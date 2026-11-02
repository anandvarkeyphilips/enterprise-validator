## Summary

<!-- What changed and why? Link the issue if one exists: "Closes #123" -->

## Changes

-

## Verification

The build enforces the quality gates, but please confirm what you actually ran:

- [ ] `./mvnw clean verify` green (unit tests, JaCoCo ≥ 90% line coverage, PIT ≥ 90% mutants killed)
- [ ] UI changes: `cd e2e && npx playwright test` green; specs updated if behaviour changed
- [ ] Manually checked against the running jar where it makes sense

## Ground rules

- [ ] No new CDN or external runtime dependencies — assets must stay self-hosted (offline use case)
- [ ] New third-party dependencies are pinned with a version and justified below
- [ ] Commits are focused and follow the existing message style (`area: summary`)

## Screenshots (for UI changes)

| Before | After |
| --- | --- |
| | |
