# Cadápio Android

Este repositório é uma base legada (Java + XML) para um desafio técnico de modernização incremental.

- App name: Cadápio Android
- Package: `com.goomer.ps`
- Min SDK: 25 (Android 7.1)
- Mock de dados local em `assets/menu.json`

## Como rodar
- Android Studio (versão estável atual) com JDK 17
- Abrir a pasta `cardapio-app/` no Android Studio
- Sincronizar Gradle
- Rodar o app no emulador/dispositivo: módulo `app` (Build/Run padrão)
- Testes: `./gradlew test`

## Desafio (o que você deve fazer)
- Migrar o fluxo da lista de produtos para Kotlin, mantendo os layouts em XML
- Escolher e implementar uma arquitetura bem definida
- Adicionar injeção de dependência (Hilt/Koin/Dagger – escolher e justificar)
- Escrever 2–5 testes unitários relevantes
- Tratar estados de loading/erro na UI atual sem mudar o layout visual
- Entregar documentação curta: `ARCHITECTURE.md` e `MIGRATION_PLAN.md`
- Bônus: CI simples (GitHub Actions), `detekt/ktlint`, `Flow/LiveData`, modularização

## Critérios
- Arquitetura (clareza de camadas, separação, dependências)
- Migração incremental e interoperabilidade (Java/XML + Kotlin convivendo)
- Qualidade e testabilidade (testes úteis, simplicidade, consistência)
- Resiliência e UX de estados (loading/erro sem alterar layout)
- Comunicação técnica (documentos e trade-offs)
- Automação (build/test; CI é bônus)

## Observações
- Não reescreva o app. Faça o mínimo necessário para tornar o fluxo sustentável e testável
- Não introduza Compose como dependência central (opcional e isolado apenas como bônus)
- Não altere o visual dos layouts

## Entregáveis

- Repositório com código funcional e histórico de commits claro
- `ARCHITECTURE.md`, `MIGRATION_PLAN.md`, `README.md` com passos de build/run e decisões e plano de migração
