package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces..IAssetStatDAO;


import br.ufpa.labes.spm.domain.AssetStat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AssetStat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetStatRepository extends IAssetStatDAO, JpaRepository<AssetStat, Long> {

}
