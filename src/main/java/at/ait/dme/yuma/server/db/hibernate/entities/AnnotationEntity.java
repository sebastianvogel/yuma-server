package at.ait.dme.yuma.server.db.hibernate.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationType;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.SemanticTag;

/**
 * A JPA database entity wrapper for an annotation object.
 * 
 * @author Rainer Simon
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "annotationentity.find.thread",
			query = "from AnnotationEntity a where a.rootId = :rootId"),	
	@NamedQuery(name = "annotationentity.find.for.object",
			query = "from AnnotationEntity a where a.objectUri = :objectUri"),	
	@NamedQuery(name = "annotationentity.count.for.object",
			query = "select count(*) from AnnotationEntity a where a.objectUri = :objectUri"),
	@NamedQuery(name = "annotationentity.count.replies",
			query = "select count(*) from AnnotationEntity a where a.parentId = :id"),
	@NamedQuery(name = "annotationentity.mostrecent",
			query = "from AnnotationEntity a order by a.lastModified"),	
	@NamedQuery(name = "annotationentity.search",
			query = "from AnnotationEntity a where (lower(a.title) like concat('%',:term,'%') or " +
					"lower(a.text) like concat('%',:term,'%'))")
})
@Table(name = "annotations")
public class AnnotationEntity implements Serializable {

	private static final long serialVersionUID = 5448003870341885100L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private Long rootId;
	
	@Column
	private Long parentId;

    @Column(length = 512)
	private String objectUri;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;
	
    @Column(length = 64)
	private String createdBy;
	
    @Column(length = 128)	
	private String title;
	
    @Column(length = 4096)		
	private String text;
	
	@Enumerated(EnumType.STRING)
	private AnnotationType type;
	
    @Column(length = 8192)
	private String fragment;
	
	@Enumerated(EnumType.STRING)
	private Scope scope;
	
	@OneToMany(mappedBy="parent",
			targetEntity=SemanticTagEntity.class, 
			fetch=FetchType.EAGER,
			cascade=CascadeType.ALL)
	private Collection<SemanticTagEntity> tags = new ArrayList<SemanticTagEntity>();

	public AnnotationEntity() { }
	
	public AnnotationEntity(Annotation a)  {
		if (!a.getRootId().isEmpty())
			this.setRootId(Long.parseLong(a.getRootId()));
		
		if (!a.getParentId().isEmpty())
			this.setParentId(Long.parseLong(a.getParentId()));
		
		this.setObjectUri(a.getObjectUri());
		this.setCreated(a.getCreated());
		this.setLastModified(a.getLastModified());
		this.setCreatedBy(a.getCreatedBy());
		this.setTitle(a.getTitle());
		this.setText(a.getText());
		this.setType(a.getType());
		this.setFragment(a.getFragment());
		this.setScope(a.getScope());
		
		for (SemanticTag t : a.getTags()) {
			this.tags.add(new SemanticTagEntity(this, t));
		}
	}

	public Annotation toAnnotation() throws AnnotationDatabaseException {
		Annotation a = new Annotation(
			Long.toString(id),
			objectUri,
			createdBy,
			created,
			lastModified,
			type,
			scope
		);

		if (rootId != null)
			a.setRootId(Long.toString(rootId));
		
		if (parentId != null)
			a.setParentId(Long.toString(parentId));
		
		a.setTitle(title);
		a.setText(text);
		a.setFragment(fragment);
		
		for (SemanticTagEntity t : tags) {
			a.addTag(t.toSemanticTag());
		}
		
		return a;
	}
	
	public void setAnnotationId(Long id) {
		this.id = id;
	}

	public Long getAnnotationId() {
		return id;
	}

	public void setRootId(Long rootId) {
		this.rootId = rootId;
	}

	public Long getRootId() {
		return rootId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setObjectUri(String objectUri) {
		this.objectUri = objectUri;
	}

	public String getObjectUri() {
		return objectUri;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setType(AnnotationType type) {
		this.type = type;
	}

	public AnnotationType getType() {
		return type;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}

	public String getFragment() {
		return fragment;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}
	
	public void setTags(final Collection<SemanticTagEntity> tags) {
		this.tags = tags;
	}
	
	public Collection<SemanticTagEntity> getTags() {
		return tags;
	}

}
