# Cardápio Android

Aplicativo de demonstração de cardápio com arquitetura moderna e migração incremental de código legado.

- App name: Cadápio Android
- Package: `com.goomer.ps`
- Min SDK: 25 (Android 7.1)
- Mock de dados local em `assets/menu.json`

## Como rodar
- Android Studio (versão estável atual) com JDK 17
- Android SDK com API 34
- Gradle 8.0+

### Executar o app
```bash
# Sincronizar dependências
./gradlew build

# Rodar testes
./gradlew test

# Executar no emulador/dispositivo
./gradlew installDebug
```

### Build variants
- `debug` - Build de desenvolvimento com logging habilitado
- `mock` - Build com dados mockados para testes
- `preProd` - Build de pré-produção
- `release` - Build de produção com ProGuard

## Arquitetura

O projeto implementa **Clean Architecture + MVVM** com as seguintes camadas:

- **Presentation**: ViewModels, Activities, Adapters (UI e estado)
- **Domain**: UseCases, Repository interfaces, Models (lógica de negócio)
- **Data**: Repository impl, DataSources, DTOs, Mappers (acesso a dados)

### Tecnologias

- **Kotlin** + **Coroutines** + **Flow/StateFlow** para programação assíncrona
- **Koin** para injeção de dependências
- **Gson** para serialização JSON
- **ktlint** + **detekt** para qualidade de código
- **GitHub Actions** para CI/CD

## Documentação

- [`ARCHITECTURE.md`](ARCHITECTURE.md) - Decisões arquiteturais e trade-offs
- [`MIGRATION_PLAN.md`](MIGRATION_PLAN.md) - Plano de migração incremental

## Entregas realizadas

✅ Migração do fluxo de lista de produtos para Kotlin
✅ Implementação de Clean Architecture + MVVM
✅ Injeção de dependências com Koin
✅ Tratamento de estados (Loading/Success/Error) com sealed classes
✅ Testes unitários (ViewModels, UseCases, Repository)
✅ CI/CD com GitHub Actions
✅ Code quality com ktlint e detekt
✅ Documentação técnica completa
