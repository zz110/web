package pers.husen.web.dao.impl;

import java.util.ArrayList;
import java.util.Date;

import pers.husen.web.bean.vo.BlogArticleVo;
import pers.husen.web.common.constants.DbConstans;
import pers.husen.web.dao.BlogArticleDao;
import pers.husen.web.dbutil.DbQueryUtils;
import pers.husen.web.dbutil.DbManipulationUtils;
import pers.husen.web.dbutil.mappingdb.BlogDetailsMapping;

/**
 * 博客文章接口实现
 * 
 * @author 何明胜
 *
 * 2017年9月17日
 */
public class BlogArticleDaoImpl implements BlogArticleDao{
	@Override
	public ArrayList<BlogArticleVo> queryAllBlogArticles() {
		String sql = "SELECT blog_id, blog_title, blog_date, "
				+ BlogDetailsMapping.BLOG_HTML_CONTENT + ", "
				+ "blog_summary FROM blog_details";
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		return DbQueryUtils.queryBeanListByParam(sql, paramList, BlogArticleVo.class);
	}
	
	@Override
	public int queryBlogTotalCount() {
		String sql = "SELECT count(*) as count FROM blog_details WHERE blog_delete = ?";
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(DbConstans.FIELD_VALID_FLAG);
		
		return DbQueryUtils.queryIntByParam(sql, paramList);
	}

	@Override
	public ArrayList<BlogArticleVo> queryBlogArticlePerPage(int pageSize, int pageNo) {
		String sql = "SELECT blog_id, blog_title, blog_date, blog_author, blog_summary, blog_read, "
				+ BlogDetailsMapping.BLOG_HTML_CONTENT + ", " 
				+ BlogDetailsMapping.BLOG_MD_CONTENT
				+ " FROM blog_details WHERE blog_delete = ?"
				+ " ORDER BY blog_date DESC LIMIT " + pageSize + " OFFSET " + (pageNo-1)*pageSize;
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(DbConstans.FIELD_VALID_FLAG);
		
		return DbQueryUtils.queryBeanListByParam(sql, paramList, BlogArticleVo.class);
	}

	@Override
	public BlogArticleVo queryPerBlogById(int blogId) {
		String sql = "SELECT blog_id, blog_title, blog_date, "
				+ BlogDetailsMapping.BLOG_HTML_CONTENT + ", "
				+ BlogDetailsMapping.BLOG_MD_CONTENT + ", "
				+ BlogDetailsMapping.BLOG_LABEL + ", "
				+ "blog_summary, blog_read, blog_author FROM blog_details"
				+ " WHERE blog_id = ? AND blog_delete = ?";
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(blogId);
		paramList.add(DbConstans.FIELD_VALID_FLAG);
		
		return DbQueryUtils.queryBeanListByParam(sql, paramList, BlogArticleVo.class).get(0);
	}

	@Override
	public int insertBlogArticle(BlogArticleVo bVo) {
		String sql = "INSERT INTO blog_details (blog_title, blog_date, "
				+ BlogDetailsMapping.BLOG_HTML_CONTENT + ", "
				+ "blog_summary, blog_author, blog_read, "
				+ BlogDetailsMapping.BLOG_MD_CONTENT + ", "
				+ BlogDetailsMapping.BLOG_LABEL + ", "
				+ BlogDetailsMapping.BLOG_DELETE
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		Object obj = null;
		paramList.add((obj = bVo.getBlogTitle()) != null ? obj : "");
		paramList.add((obj = bVo.getBlogDate()) != null ? obj : new Date());
		paramList.add((obj = bVo.getBlogHtmlContent()) != null ? obj : "");
		paramList.add((obj = bVo.getBlogSummary()) != null ? obj : "");
		paramList.add((obj = bVo.getBlogAuthor()) != null ? obj : "");
		paramList.add((obj = bVo.getBlogRead()) != null ? obj : 0);
		paramList.add((obj = bVo.getBlogMdContent()) != null ? obj : "");
		paramList.add((obj = bVo.getBlogLabel()) != null ? obj : "");
		paramList.add(DbConstans.FIELD_VALID_FLAG);
		
		return DbManipulationUtils.insertNewRecord(sql, paramList);
	}

	@Override
	public int updateBlogReadById(int blogId) {
		String sql = "UPDATE blog_details SET blog_read = ((SELECT blog_read FROM blog_details where blog_id = ?) + 1) WHERE blog_id = ?";
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(blogId);
		paramList.add(blogId);
		
		return DbManipulationUtils.updateRecordByParam(sql, paramList);
	}

	@Override
	public int updateBlogById(BlogArticleVo bVo) {
		String sql = "UPDATE " + BlogDetailsMapping.DB_NAME + " SET "
				+ BlogDetailsMapping.BLOG_TITLE + "=?, "
				+ BlogDetailsMapping.BLOG_AUTHOR + "=?, "
				+ BlogDetailsMapping.BLOG_SUMMARY + "=?, "
				+ BlogDetailsMapping.BLOG_HTML_CONTENT + "=?, "
				+ BlogDetailsMapping.BLOG_MD_CONTENT + "=?, "
				+ BlogDetailsMapping.BLOG_LABEL + "=? "
				+ "WHERE "
				+ BlogDetailsMapping.BLOG_ID + "=?";
		
		ArrayList<Object> paramList = new ArrayList<Object>();
		
		paramList.add(bVo.getBlogTitle());
		paramList.add(bVo.getBlogAuthor());
		paramList.add(bVo.getBlogSummary());
		paramList.add(bVo.getBlogHtmlContent());
		paramList.add(bVo.getBlogMdContent());
		paramList.add(bVo.getBlogLabel());

		paramList.add(bVo.getBlogId());
		
		return DbManipulationUtils.updateRecordByParam(sql, paramList);
	}

	@Override
	public int logicDeleteBlogById(int blogId) {
		String sql = "UPDATE blog_details SET blog_delete = 1 WHERE blog_id = ?";
		ArrayList<Object> paramList = new ArrayList<Object>();
		paramList.add(blogId);
		
		return DbManipulationUtils.deleteRecordByParamLogic(sql, paramList);
	}
}