package br.ufpa.labes.spm.repository.interfaces.assets;

import java.util.List;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.TagStats;
import org.qrconsult.spm.util.PagingContext;
import org.qrconsult.spm.util.SortCriteria;


@Local
public interface ITagStatsDAO extends IBaseDAO<TagStats, Long> {

	public List<TagStats> retrieveMostVotedTags(int numResults);
	public List<TagStats> retrieveMostVotedTagsForAsset(String assetUid, int numResults);

	@Override
	@Deprecated
	public TagStats retrieve(Long key);
	@Override
	@Deprecated
	public List<TagStats> retrieveByCriteria(TagStats searchCriteria);
	@Override
	@Deprecated
	public List<TagStats> retrieveByCriteria(TagStats searchCriteria,
			SortCriteria sortCriteria);
	@Override
	@Deprecated
	public List<TagStats> retrieveByCriteria(TagStats searchCriteria,
			SortCriteria sortCriteria, PagingContext paging);

}