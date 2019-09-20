package br.ufpa.labes.spm.repository.impl.assets;

import java.util.List;

import javax.persistence.Query;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.assets.ITagStatsDAO;
import br.ufpa.labes.spm.domain.TagStats;

public class TagStatsDAO extends BaseDAO<TagStats, String> implements ITagStatsDAO {

  protected TagStatsDAO() {
    super(TagStats.class);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<TagStats> retrieveMostVotedTags(int numResults) {
    String queryStr = "from " + TagStats.class.getName() + " as tagStats order by tagStats.count";

    Query query = this.getPersistenceContext().createQuery(queryStr);
    query.setMaxResults(numResults);

    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<TagStats> retrieveMostVotedTagsForAsset(String assetUid, int numResults) {
    String queryStr =
        "from "
            + TagStats.class.getName()
            + " as tagStats "
            + "where tagStats.asset.uid = :assetUid order by tagStats.count";

    Query query = this.getPersistenceContext().createQuery(queryStr);
    query.setParameter("assetUid", assetUid);
    query.setMaxResults(numResults);

    return query.getResultList();
  }
}
