package at.ait.dme.yuma.server.db.hibernate.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;

import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationType;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.SemanticTag;

@Entity
public class AnnotationEntity implements Serializable {

	private static final long serialVersionUID = 5448003870341885100L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long annotationId;	
	
	@Column
	private Long rootId;
	
	@Column
	private Long parentId;

    @Column(length = 512, nullable = false)
    @Index(name = "objectId")
	private String objectId;

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
	
    @OneToMany
    @JoinColumn(name="annotationId")
	private List<SemanticTag> tags;

	public AnnotationEntity(Annotation a)  {
		this.rootId = Long.parseLong(a.getRootId());
		this.parentId = Long.parseLong(a.getParentId());
		this.objectId = a.getObjectID();
		this.created = a.getCreated();
		this.lastModified = a.getLastModified();
		this.createdBy = a.getCreatedBy();
		this.title = a.getTitle();
		this.text = a.getText();
		this.type = a.getType();
		this.fragment = a.getFragment();
		this.scope = a.getScope();
		this.tags = a.getTags();
	}
	
	public Annotation toAnnotation() {
		// TODO implement
		return null;
	}

}
