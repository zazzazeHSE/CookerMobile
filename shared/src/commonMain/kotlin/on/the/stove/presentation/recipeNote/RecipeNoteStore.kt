@file:OptIn(FlowPreview::class, ObsoleteCoroutinesApi::class)

package on.the.stove.presentation.recipeNote

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import on.the.stove.database.AppDatabaseRepository
import on.the.stove.dispatchers.ioDispatcher
import on.the.stove.dto.Note
import on.the.stove.presentation.BaseStore
import org.koin.core.component.inject

class RecipeNoteStore(private val recipeId: String) :
    BaseStore<RecipeNoteState, RecipeNoteAction, RecipeNoteEffect>() {

    private val appDatabaseRepository: AppDatabaseRepository by inject()

    override val stateFlow: MutableStateFlow<RecipeNoteState> =
        MutableStateFlow(RecipeNoteState(recipeId = recipeId))
    override val sideEffectsFlow: MutableSharedFlow<RecipeNoteEffect> = MutableSharedFlow()

    private val noteContentSharedFlow = MutableSharedFlow<String>()
    private val noteFlow = noteContentSharedFlow
        .distinctUntilChanged()
        .debounce(300L)

    init {
        scope.launch(ioDispatcher) {
            noteFlow.collect { content ->
                Note(
                    id = recipeId,
                    content = content,
                ).also(appDatabaseRepository::updateNode)
            }
        }
    }

    override suspend fun reduce(action: RecipeNoteAction, initialState: RecipeNoteState) {
        when (action) {
            is RecipeNoteAction.Init -> {
                val note = appDatabaseRepository.getNote(stateFlow.value.recipeId)

                updateState { state ->
                    state.copy(
                        content = note?.content.orEmpty()
                    )
                }
            }
            is RecipeNoteAction.SetContent -> {
                updateState { state ->
                    state.copy(
                        content = action.content
                    )
                }
                noteContentSharedFlow.emit(action.content)
            }
        }
    }
}