Demo for Bug in ContextPathCompositeHandler
===========================================

Application has a 'bugged' and a 'fixed' profile.

Bugged profile uses the original `ContextPathCompositeHandler`.

Fixed profile uses the `FixedContextPathCompositeHandler` which is
identical other than in returning the deferred response completion.

Tests run the two profiles.

`TestBuggedApplication` exhibits current behaviour and always fails.

`TestFixedApplication` uses the modified behaviour and works.