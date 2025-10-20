# MIGRATION_PLAN

## Objetivo
Migrar fluxo de lista de produtos para Kotlin + MVVM + Clean Architecture + Koin, mantendo layouts XML e interoperabilidade Java/Kotlin.

## Fases de Implementação

### Fase 1: Base Arquitetural
- [x] Configurar Kotlin + dependências (Koin, Coroutines, Flow, Gson)
- [x] Criar estrutura de pacotes Clean Architecture:
  ```
  presentation/ (Activities, ViewModels, Adapters)
  domain/ (UseCases, Models, Repository interfaces)
  data/ (Repository impl, DataSources, DTOs)
  ```
- [x] Migrar `MenuItem` para Kotlin data class com Gson annotations
  - Gson annotarions ficou como débito técnico para a próxima fase por motivos de interoperabilidade

### Fase 2: Camada de Dados
- [x] Repository pattern (`MenuRepository` interface + `MenuRepositoryImpl`)
- [x] DataSource (`LocalMenuDataSource` - migrado de `LegacyDataSource`)
- [x] DTOs com Gson (`MenuItemDto`, `MenuResponseDto`)
- [x] Mappers (`MenuItemMapper` - DTO -> Domain)

### Fase 3: Camada de Domínio
- [x] Use Case (`GetMenuItemsUseCase`, `GetMenuItemByIdUseCase`)
- [x] Domain models (`MenuItem`, `MenuResult`)
- [x] Repository interface (`MenuRepository`)

### Fase 4: Camada de Apresentação
- [x] ViewModel (`MenuListViewModel`) com StateFlow
- [x] UI States (`MenuListUiState` sealed class)
- [x] Migrar `MenuListActivity` para Kotlin + ViewBinding
- [x] Migrar `MenuAdapter` para Kotlin + DiffUtil

### Fase 5: Injeção de Dependências
- [x] Koin modules:
  - `AppModule` (Application scope)
  - `DataModule` (Repository, DataSource)
  - `PresentationModule` (ViewModels)
- [x] Configurar Application class com Koin

### Fase 6: Testes
- [x] Testar `MenuListViewModel` (estados Loading/Success/Error)
- [x] Testar `MenuRepository` (mapeamento DTO -> Domain)
- [x] Testar `GetMenuItemsUseCase` (lógica de negócio)
- [x] Testar Koin modules (grafo de dependências)

### Fase 7: Qualidade
- [x] Configurar detekt + ktlint
- [x] GitHub Actions CI
- [ ] Limpar warnings e aplicar sugestões

### Fase 8: Documentação
- [ ] `ARCHITECTURE.md` completo
- [ ] Atualizar `README.md`

## Decisões Técnicas

### Arquitetura: Clean Architecture
**Prós:** Separação clara de responsabilidades, testabilidade, escalabilidade
**Contras:** Mais código inicial, curva de aprendizado
**Justificativa:** Padrões enterprise, facilita manutenção

### DI: Koin (vs Hilt)
**Prós:** Simplicidade, menos boilerplate, compatibilidade com KMP
**Contras:** Erros em runtime, não é padrão Google
**Justificativa:** Velocidade de desenvolvimento, Facilidade de migração gradual, Compatibilidade com KMP

### Serialização: Gson (vs Moshi)
**Prós:** Curva de aprendizado menor, maior comunidade
**Contras:** Mais reflection, menos performance vs Moshi
**Justificativa:** Melhor para migração

### Reatividade: Kotlin Flow + StateFlow
**Prós:** Moderno, integração com Coroutines, melhor performance
**Contras:** Curva de aprendizado vs LiveData
**Justificativa:** Padrão futuro do Android

### Estrutura: DTOs + Mappers
**Prós:** Separação clara entre dados e domínio, facilita testes
**Contras:** Mais classes, boilerplate de mapeamento
**Justificativa:** Melhora testabilidade

## Trade-offs Assumidos
- **Runtime DI errors:** Mitigado por testes do grafo de DI
- **Moshi vs Gson:** Curva de aprendizado menor, mas performance inferior
- **Clean Architecture:** Mais código inicial, mas melhor organização
- **Manter XML layouts:** Requisito do desafio, foco na arquitetura
- **Migração incremental:** Não reescrever tudo, manter interoperabilidade

## Bonus: Sugestões
- **SDUI:** Atualizar arquitetura do mobile para usar SDUI e com isso mitigar problema de atraso no tempo de ajustes de erros em PRD e ajudar em testes A/B e personalização da experiência do usuário
- **KMP** Estudar a viabilidade do uso de Kotlin Multiplataforma visando a facilidade de manter uma única base de código para as diferentes frentes
