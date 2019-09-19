package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.assets.IAssetDAO;


import br.ufpa.labes.spm.domain.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Asset entity.
 */
@Repository
public interface AssetRepository extends IAssetDAO, JpaRepository<Asset, Long> {

    @Query(value = "select distinct asset from Asset asset left join fetch asset.favoritedBies left join fetch asset.followers left join fetch asset.collaborators",
        countQuery = "select count(distinct asset) from Asset asset")
    Page<Asset> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct asset from Asset asset left join fetch asset.favoritedBies left join fetch asset.followers left join fetch asset.collaborators")
    List<Asset> findAllWithEagerRelationships();

    @Query("select asset from Asset asset left join fetch asset.favoritedBies left join fetch asset.followers left join fetch asset.collaborators where asset.id =:id")
    Optional<Asset> findOneWithEagerRelationships(@Param("id") Long id);

}
