# 13 — Extensions / Future Features

> Ideas beyond the minimum scope. Useful to differentiate the project and for the final discussion.
> Do not implement without closing the mandatory scope first.

## Candidates

| Idea | User value | Effort | Depends on |
|---|---|---|---|
| Roadmap Sharing |	Share roadmaps with other users through links or exports | High |
| Progress Analytics |	Visual charts showing learning progress and completion rates | Medium |
| Notifications & Reminders |	Scheduled reminders for pending tasks | Medium |
| Collaborative Roadmaps |	Multiple users editing the same roadmap | Medium |
| AI Task Recommendations |	Suggest next tasks based on user progress | Medium |
| Calendar Integration |	Sync roadmap milestones with Google Calendar | Low |

## Known technical debt

| Item | Where | Why it ended up like this | How to fix |
|---|---|---|---|
| AI Response Parsing  | Gemini AI integration layer | Basic validation was sufficient for the initial implementation and allowed faster development of the AI feature | Add stronger schema validation and structured response checking |
| Testing Coverage | Unit and UI testing | Development time was prioritized toward implementing core features rather than comprehensive automated testing | Increase unit and UI test coverage |
| Error Handling | Application-wide error reporting | Generic error messages were used to simplify implementation during early development | Introduce centralized error management and more user-friendly feedback |
| Modularization | Overall application architecture | Features were kept within a single application module to reduce complexity during development | Split functionality into separate feature modules for better scalability and maintainability |

## Discarded ideas (and why)

- Social Feed (increase project complexity and move the focus away from the roadmap planning)
- Gamification System (outside project's core objectives)
- Real-Time Collaboration (requires backend infrastructure and conflict-resolution mechanics outside project scope)