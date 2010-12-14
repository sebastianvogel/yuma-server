package at.ait.dme.yuma.server.db.hibernate.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import at.ait.dme.yuma.server.model.SemanticTag;

/**
 * A JPA database entity wrapper for a SemanticTag object.
 * 
 * @author Rainer Simon
 */
@Entity
@Table(name = "tags")
public class SemanticTagEntity implements Serializable {

	private static final long serialVersionUID = -7648256413169945758L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="annotations_id")
	private AnnotationEntity parent;
	
	@Column
	private String uri;

	@Column
	private String primaryLabel;

	@Column
	private String primaryDescription;
	
	@Column
	private String primaryLang;
	
	@Column
	private String type;
	
	@Column
	private SemanticRelationEntity relation;
	
	// private Map<String, String> altLabels;
	
	// private Map<String, String> altDescriptions;
	
	public SemanticTagEntity() { }
	
	public SemanticTagEntity(AnnotationEntity parent, SemanticTag t) {
		this.setParent(parent);
		this.setUri(t.getURI().toString());
		this.setPrimaryLabel(t.getPrimaryLabel());
		this.setPrimaryDescription(t.getPrimaryDescription());
		this.setPrimaryLang(t.getPrimaryLanguage());
		this.setType(t.getType());
		this.setRelation(new SemanticRelationEntity(t.getRelation()));
	}
	
	public SemanticTag toSemanticTag() {
		return null;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getd() {
		return id;
	}
	
	public void setParent(AnnotationEntity parent) {
		this.parent = parent;
	}

	public AnnotationEntity getParent() {
		return parent;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setPrimaryLabel(String primaryLabel) {
		this.primaryLabel = primaryLabel;
	}

	public String getPrimaryLabel() {
		return primaryLabel;
	}

	public void setPrimaryDescription(String primaryDescription) {
		this.primaryDescription = primaryDescription;
	}

	public String getPrimaryDescription() {
		return primaryDescription;
	}

	public void setPrimaryLang(String primaryLang) {
		this.primaryLang = primaryLang;
	}

	public String getPrimaryLang() {
		return primaryLang;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setRelation(SemanticRelationEntity relation) {
		this.relation = relation;
	}

	public SemanticRelationEntity getRelation() {
		return relation;
	}

}
