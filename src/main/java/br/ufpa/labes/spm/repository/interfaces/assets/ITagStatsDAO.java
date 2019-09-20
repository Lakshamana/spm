package br.ufpa.labes.spm.repository.interfaces.assets;

import java.util.List;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.TagStats;
import br.ufpa.labes.spm.util.PagingContext;
import br.ufpa.labes.spm.util.SortCriteria;

public interface ITagStatsDAO extends IBaseDAO<TagStats, String> {

  public List<TagStats> retrieveMostVotedTags(int numResults);

  public List<TagStats> retrieveMostVotedTagsForAsset(String assetUid, int numResults);

  @Override
  @Deprecated
  public TagStats retrieve(String key);

  @Override
  @Deprecated
  public List<TagStats> retrieveByCriteria(TagStats searchCriteria);

  @Override
  @Deprecated
  public List<TagStats> retrieveByCriteria(TagStats searchCriteria, SortCriteria sortCriteria);

  @Override
  @Deprecated
  public List<TagStats> retrieveByCriteria(
      TagStats searchCriteria, SortCriteria sortCriteria, PagingContext paging);
}
