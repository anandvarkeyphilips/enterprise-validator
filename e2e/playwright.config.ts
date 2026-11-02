import { defineConfig } from '@playwright/test';

const appPort = process.env.APP_PORT ?? '8080';
// The Spring Boot jar built by ./mvnw -DskipTests package (override for CI artifacts)
const appJar = process.env.APP_JAR ?? '../target/enterprise-validator-2.4.0.jar';

export default defineConfig({
  testDir: './tests',
  fullyParallel: false,
  timeout: 30_000,
  expect: { timeout: 10_000 },
  retries: process.env.CI ? 1 : 0,
  reporter: process.env.CI ? [['github'], ['html', { open: 'never' }]] : [['list']],
  use: {
    baseURL: `http://localhost:${appPort}`,
    screenshot: 'only-on-failure',
    trace: 'retain-on-failure',
    permissions: ['clipboard-write'],
  },
  webServer: {
    command: `java -jar ${appJar} --server.port=${appPort} --spring.pid.file=target-e2e.pid --logging.file.name=target-e2e.log`,
    url: `http://localhost:${appPort}/editor`,
    reuseExistingServer: !process.env.CI,
    timeout: 120_000,
  },
});
