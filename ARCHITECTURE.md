# ARCHITECTURE

## Decisões arquiteturais
- **Padrão adotado**: Clean Architecture + MVVM
- **Camadas**:
  - `presentation/`: ViewModels com StateFlow, Activities, Adapters. Observa domain, não conhece data
  - `domain/`: UseCases, Repository interfaces, Models de domínio. Camada pura Kotlin sem dependências Android
  - `data/`: Repository impl, DataSources, DTOs, Mappers. Conhece domain, implementa contratos
- **DI**: Koin - escolhido por simplicidade, menos boilerplate, migração gradual e compatibilidade KMP
- **Threading/reatividade**: Coroutines + Flow/StateFlow - padrão moderno, melhor que LiveData para casos complexos

## Testabilidade e qualidade
- Testes unitários: ViewModels, UseCases, Repository, validação de DI modules
- ktlint + detekt para code style consistente
- GitHub Actions CI rodando testes e análise estática
- DiffUtil no adapter para performance
- Sealed class `CardapioResult<T>` para type-safe state handling

## Trade-offs relevantes
- Migração incremental mantendo Java legado convivendo com Kotlin (menos risco, entrega gradual)
    - Evitei recriar a lógica da UI da home pois já era consistente
    - Evitei recriar a lógica do Adapter, adicionando apenas modernidade com viewBinding e segurança/performance com DiffUtil
    - Evitei alterações na tela de detalhes para simular a interoperabilidade com telas legadas em java (no final apenas passei os textos hardcodeds para o strings.xml)
- Gson mantido vs Moshi (menor curva de aprendizado)
- Koin vs Hilt (erros em runtime mitigados por testes, mas setup mais simples)
- XML layouts preservados conforme requisito (foco na arquitetura, não UI)
- SavedStateHandle implementado para sobreviver a process death

## Próximas etapas
- Fragments + Navigation Component
- Modularização por feature
- Migração gradual para Compose (coexistindo com XML)
    - Durante a migração para o compose é necessária a discução sobre a alteração da arquitetura de MVVM para MVI
- SDUI (Server-Driven UI) para flexibilidade de A/B tests e redução de deploys
- KMP para compartilhar lógica de negócio entre Android/iOS/Web
