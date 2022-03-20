package on.the.stove.presentation.recipeNote

data class RecipeNoteState(
    val recipeId: String,
    val content: String = "",
)

sealed class RecipeNoteAction {

    object Init : RecipeNoteAction()

    data class SetContent(val content: String) : RecipeNoteAction()
}

sealed class RecipeNoteEffect {
    // Empty
}

