# 15 — Postmortem & Reflection (final)

## Summary

The Master Planner project was developed as an Android application focused on helping users create and manage personalized learning roadmaps. The application combines manual task management with AI-assisted roadmap generation using Gemini. The project was implemented using modern Android development practices, including Jetpack Compose for the UI, Room for local persistence, Hilt for dependency injection, and a clean architecture structure separating presentation, domain, and data layers.

The final product successfully delivers the core functionality outlined in the project vision, including roadmap creation, task management, progress tracking, local data persistence, and AI-generated roadmap suggestions.

## What went well

- The clean architecture approach helped maintain a clear separation of concerns and made the codebase easier to organize and extend.
- Jetpack Compose significantly simplified UI development and enabled rapid iteration of screens and user interactions.
- Room integration provided reliable offline-first functionality with minimal complexity.
- Hilt reduced boilerplate and improved dependency management across the application.
- Gemini integration successfully demonstrated how AI can enhance user productivity by generating structured learning plans.
- Documentation and ADRs helped maintain consistency in architectural decisions throughout development.

## What went badly / what cost the most

- Integrating AI-generated content required additional validation and prompt engineering to ensure consistent output quality.
- Managing state across multiple Compose screens initially led to unnecessary recompositions and increased complexity.
- Time constraints limited the implementation of advanced features such as cloud synchronization, collaborative roadmaps, and more comprehensive analytics.
- Testing coverage is lower than desired due to prioritizing feature completion during the final stages of development.

## Working with AI — reflection

AI tools were used extensively throughout the project as development assistants. They helped with architecture discussions, code generation, bug investigation, documentation drafting, and prompt refinement for Gemini integration.

The most valuable use cases were:

- Generating boilerplate code.
- Explaining Android framework concepts.
- Reviewing architectural decisions.
- Assisting with documentation writing.

However, AI-generated code was never accepted blindly. All generated outputs required review, testing, and adaptation to fit the project's architecture and coding standards. This reinforced the importance of understanding generated solutions rather than relying on them directly.

## Architecture decisions — retrospective

The decision to adopt Clean Architecture with MVVM proved beneficial. The separation between UI, business logic, and data layers improved maintainability and reduced coupling between components.

Using Hilt instead of Koin was also a positive choice because it integrated naturally with Android tooling and provided compile-time dependency validation.

If the project were continued, additional modularization could be introduced to separate AI functionality, roadmap management, and user preferences into independent modules.

## Metrics (if applicable)

### Functional Metrics

- Core roadmap management implemented.
- AI-assisted roadmap generation implemented.
- Offline persistence implemented.
- Task progress tracking implemented.
- Material Design 3 user interface implemented.

### Technical Metrics

- Architecture: Clean Architecture + MVVM.
- UI Framework: Jetpack Compose.
- Dependency Injection: Hilt.
- Database: Room.
- AI Integration: Gemini API.

### Documentation

- Project vision completed.
- Architecture documentation completed.
- ADR documentation completed.
- AI usage log maintained throughout development.

- Number of commits, % with `AI-Assisted` ≠ none, bugs found in generated code, etc.

## Key lessons (3–5 bullets)

1. Planning architecture early reduces technical debt later.
2. Dependency injection significantly improves maintainability in medium-sized projects.
3. AI-assisted features require careful prompt design and output validation.
4. Documentation should be maintained continuously rather than completed at the end.
5. Jetpack Compose accelerates UI development but requires a strong understanding of state management principles.