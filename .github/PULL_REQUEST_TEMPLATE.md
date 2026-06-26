## Description

<!-- What does this PR do? Link to related issue(s) if applicable. -->

Closes #

## Type of Change

- [ ] Bug fix
- [ ] New feature
- [ ] Enhancement to existing feature
- [ ] New provider implementation
- [ ] Refactoring (no functional changes)
- [ ] Documentation
- [ ] Other: ___

## Checklist

- [ ] `mvn clean compile` passes
- [ ] Existing tests pass (`mvn test`)
- [ ] New tests added (if applicable)
- [ ] README updated (if applicable)
- [ ] No credentials, API keys, or secrets in committed files

### Provider-specific (if applicable)

- [ ] Provider interface implemented
- [ ] Descriptor registered via `META-INF/services/`
- [ ] Configuration fields defined in descriptor
- [ ] `README.md` with required configuration documented
- [ ] `LICENSE` file included

### DynamoDB changes (if applicable)

- [ ] Migration path considered for existing data
- [ ] Backward-compatible key lookups added (if range key changed)
- [ ] `DynamoDbSchema.java` updated

## Testing

<!-- How was this tested? LocalStack, unit tests, manual? -->
