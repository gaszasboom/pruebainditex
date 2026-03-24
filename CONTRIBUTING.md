# Contributing Guide

## Control de versiones — Conventional Commits

Este proyecto sigue el estándar [Conventional Commits](https://www.conventionalcommits.org/) para los mensajes de commit.

### Formato

```
<tipo>(<ámbito>): <descripción corta>
```

El `<ámbito>` es opcional e indica la parte del proyecto afectada.

### Tipos permitidos

| Tipo       | Cuándo usarlo                                              |
|------------|------------------------------------------------------------|
| `feat`     | Nueva funcionalidad                                        |
| `fix`      | Corrección de un bug                                       |
| `refactor` | Cambio de código que no añade funcionalidad ni corrige bug |
| `test`     | Añadir o corregir tests                                    |
| `docs`     | Cambios en documentación                                   |
| `chore`    | Tareas de mantenimiento (dependencias, configuración...)   |
| `perf`     | Mejora de rendimiento                                      |
| `ci`       | Cambios en pipelines de integración continua               |

### Ejemplos

```
feat(prices): add applicable price endpoint
fix(domain): validate negative brand id
refactor(persistence): extract PriceEntityMapper
test(controller): add integration tests for error cases
docs(readme): update health endpoint url
chore(pom): remove empty xml fields
perf(repository): use native query with LIMIT 1
```

### Ramas

Seguir la convención `<tipo>/<descripción-en-kebab-case>`:

```
feat/applicable-price-endpoint
fix/negative-brand-id-validation
refactor/extract-price-entity-mapper
```
