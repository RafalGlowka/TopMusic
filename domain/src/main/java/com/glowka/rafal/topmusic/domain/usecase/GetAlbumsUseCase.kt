package com.glowka.rafal.topmusic.domain.usecase

import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import kotlinx.coroutines.flow.Flow

interface GetAlbumsUseCase : UseCase<EmptyParam, List<Album>>

class GetAlbumsUseCaseImpl(
  val musicRepository: MusicRepository,
) : GetAlbumsUseCase {

  override fun invoke(param: EmptyParam): Flow<UseCaseResult<List<Album>>> {
    return musicRepository.getAlbums()
  }
}