package br.ufpa.labes.spm.repository.interfaces.assets;

import java.util.List;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Asset;
import br.ufpa.labes.spm.domain.AssetRelationship;
// import org.qrconsult.spm.vo.AssetCatalog;

public interface IAssetDAO extends IBaseDAO<Asset, String> {

  public List<AssetRelationship> retrieveRelatedAssetsTo(String assetUid);

  /** Ver todas as consultas necess�rias */

  // Cat�logo de ativos
  // public AssetCatalog retrieveAssetForCatalog(AssetCatalog catalog);
}
