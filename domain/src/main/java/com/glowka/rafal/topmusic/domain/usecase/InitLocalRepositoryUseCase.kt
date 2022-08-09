package com.glowka.rafal.topmusic.domain.usecase

import com.glowka.rafal.topmusic.domain.repository.MusicRepository
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import kotlinx.coroutines.flow.Flow

interface InitLocalRepositoryUseCase : UseCase<EmptyParam, Boolean>

class InitLocalRepositoryUseCaseImpl(
  val musicRepository: MusicRepository,
) : InitLocalRepositoryUseCase {

  override fun invoke(param: EmptyParam): Flow<UseCaseResult<Boolean>> {
    return musicRepository.init()
  }
}