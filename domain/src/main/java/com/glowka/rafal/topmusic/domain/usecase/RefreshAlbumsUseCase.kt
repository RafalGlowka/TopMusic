package com.glowka.rafal.topmusic.domain.usecase

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import kotlinx.coroutines.flow.Flow

interface RefreshAlbumsUseCase : UseCase<EmptyParam, List<Album>>

class RefreshAlbumsUseCaseImpl(
  val musicRepository: MusicRepository,
) : RefreshAlbumsUseCase {

  override fun invoke(param: EmptyParam): Flow<UseCaseResult<List<Album>>> {
    return musicRepository.refreshListFromNetwork()
  }
}