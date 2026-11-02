import { expect, test } from '@playwright/test';

/**
 * End-to-end tests for the Enterprise Validator editor. The app is started from
 * the packaged jar by Playwright's webServer (see playwright.config.ts).
 *
 * The editor is an Ace instance exposed as a global `editor` object by
 * static/js/editor.js, so input is set through evaluate() rather than typing.
 */

async function setEditorValue(page: import('@playwright/test').Page, value: string) {
  await page.evaluate((v) => {
    (window as unknown as { editor: { setValue: (s: string, c: number) => void; clearSelection: () => void } })
      .editor.setValue(v, 0);
    (window as unknown as { editor: { clearSelection: () => void } }).editor.clearSelection();
  }, value);
}

function getEditorValue(page: import('@playwright/test').Page): Promise<string> {
  return page.evaluate(() =>
    (window as unknown as { editor: { getValue: () => string } }).editor.getValue());
}

test.beforeEach(async ({ page }) => {
  await page.goto('/editor');
});

test.describe('editor page', () => {
  test('root path redirects to the editor', async ({ page }) => {
    await page.goto('/');
    await expect(page).toHaveURL(/\/editor$/);
    await expect(page.locator('p.title')).toHaveText('Enterprise Validator');
  });

  test('loads with title, hero and preloaded YAML sample', async ({ page }) => {
    await expect(page).toHaveTitle('Enterprise Validator');
    await expect(page.locator('p.title')).toHaveText('Enterprise Validator');
    const editorValue = await getEditorValue(page);
    expect(editorValue).toContain('server:');
    expect(editorValue).toContain('port: 8090');
  });

  test('shows all nine action buttons', async ({ page }) => {
    for (const id of ['encodeData', 'decodeData', 'validateYamlData', 'validateJsonData',
      'formatJsonData', 'formatXmlData', 'diffJsonData', 'diffYamlData', 'copyData']) {
      await expect(page.locator(`#${id}`)).toBeVisible();
    }
  });

  test('renders the custom 404 page for unknown routes', async ({ page }) => {
    const response = await page.goto('/no-such-page');
    expect(response?.status()).toBe(404);
    await expect(page.locator('body')).toContainText("We couldn't find what you were looking for.");
  });

  test('exposes a healthy actuator endpoint', async ({ page }) => {
    const response = await page.request.get('/actuator/health');
    expect(response.status()).toBe(200);
    expect(await response.json()).toMatchObject({ status: 'UP' });
  });

  test('serves the OpenAPI spec and Swagger UI', async ({ page }) => {
    const apiDocs = await page.request.get('/v3/api-docs');
    expect(apiDocs.status()).toBe(200);
    const spec = await apiDocs.json();
    expect(spec.openapi).toContain('3.');
    expect(JSON.stringify(spec.paths)).toContain('/yaml');
    await page.goto('/swagger-ui.html');
    await expect(page.locator('div.topbar').first()).toBeVisible();
    await expect(page.locator('div.swagger-ui').first()).toBeVisible();
  });
});

test.describe('YAML validation', () => {
  test('valid YAML reports success', async ({ page }) => {
    await setEditorValue(page, 'server:\n  port: 8090\n');
    await page.click('#validateYamlData');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveText('Valid YAML!!!');
    await expect(result).toHaveClass(/validation-message-success/);
  });

  test('invalid YAML reports failure with parser message', async ({ page }) => {
    await setEditorValue(page, 'Wrong : Data :');
    await page.click('#validateYamlData');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveClass(/validation-message-failure/);
    await expect(result).toContainText('mapping values are not allowed here');
  });
});

test.describe('JSON validation and formatting', () => {
  test('valid JSON reports success', async ({ page }) => {
    await setEditorValue(page, '{"glossary": {"title": "example glossary"}}');
    await page.click('#validateJsonData');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveText('Valid JSON!!!');
    await expect(result).toHaveClass(/validation-message-success/);
  });

  test('invalid JSON reports failure with line information', async ({ page }) => {
    await setEditorValue(page, '{"glossary": {"title": "example glossary"');
    await page.click('#validateJsonData');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveClass(/validation-message-failure/);
    await expect(result).toContainText('Expected');
  });

  test('format JSON pretty prints with four space indent', async ({ page }) => {
    await setEditorValue(page, '{"glossary":{"title":"example glossary"}}');
    await page.click('#formatJsonData');
    await expect(page.locator('#validationResultBlock')).toHaveText('Formatted JSON!!!');
    await expect.poll(() => getEditorValue(page)).toBe('{\n    "glossary": {\n        "title": "example glossary"\n    }\n}');
  });
});

test.describe('XML formatting', () => {
  test('format XML pretty prints the document', async ({ page }) => {
    await setEditorValue(page, '<book><title>java book</title></book>');
    await page.click('#formatXmlData');
    await expect(page.locator('#validationResultBlock')).toHaveText('Formatted XML!!!');
    const value = await getEditorValue(page);
    expect(value).toContain('<book>');
    expect(value).toContain('    <title>java book</title>');
  });

  test('invalid XML reports failure', async ({ page }) => {
    await setEditorValue(page, '<book><title>unclosed</book>');
    await page.click('#formatXmlData');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveClass(/validation-message-failure/);
  });
});

test.describe('copy', () => {
  test('copy button copies the editor content and confirms', async ({ page }) => {
    await setEditorValue(page, 'Anand');
    await page.click('#copyData');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveClass(/validation-message-success/);
    await expect(result).toContainText('copied to the clipboard');
  });
});

test.describe('Base64 encoding', () => {
  test('encodes plain text to Base64', async ({ page }) => {
    await setEditorValue(page, 'Anand');
    await page.click('#encodeData');
    await expect(page.locator('#validationResultBlock')).toHaveText('Encode Successful!!!');
    await expect.poll(() => getEditorValue(page)).toBe('QW5hbmQ=');
  });

  test('decodes Base64 to plain text', async ({ page }) => {
    await setEditorValue(page, 'QW5hbmQ=');
    await page.click('#decodeData');
    await expect(page.locator('#validationResultBlock')).toHaveText('Decode Successful!!!');
    await expect.poll(() => getEditorValue(page)).toBe('Anand');
  });
});

test.describe('document diff', () => {
  test('diff JSON opens side-by-side pane and reports key differences', async ({ page }) => {
    await setEditorValue(page, '{"a":1,"b":{"c":2}}');
    await page.click('#diffJsonData');
    const pane = page.locator('#comparePane');
    await expect(pane).toBeVisible();
    await page.evaluate((v) => {
      (window as unknown as { compareEditor: { setValue: (s: string, c: number) => void; clearSelection: () => void } })
        .compareEditor.setValue(v, 0);
      (window as unknown as { compareEditor: { clearSelection: () => void } }).compareEditor.clearSelection();
    }, '{"a":2,"b":{"d":3}}');
    await page.click('#diffRun');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveClass(/validation-message-failure/);
    await expect(result).toContainText('3 key difference(s) found');
    await expect(result).toContainText('~ $.a: expected 1, found 2');
    await expect(result).toContainText('+ $.b.d: added 3');
    await expect(pane).toBeVisible();
    await page.click('#diffClose');
    await expect(pane).toBeHidden();
  });

  test('diff YAML reports key-identical documents', async ({ page }) => {
    await setEditorValue(page, 'a: 1\n');
    await page.click('#diffYamlData');
    await page.evaluate((v) => {
      (window as unknown as { compareEditor: { setValue: (s: string, c: number) => void; clearSelection: () => void } })
        .compareEditor.setValue(v, 0);
      (window as unknown as { compareEditor: { clearSelection: () => void } }).compareEditor.clearSelection();
    }, 'a: 1\n');
    await page.click('#diffRun');
    const result = page.locator('#validationResultBlock');
    await expect(result).toHaveClass(/validation-message-success/);
    await expect(result).toContainText('key-identical');
  });
});
