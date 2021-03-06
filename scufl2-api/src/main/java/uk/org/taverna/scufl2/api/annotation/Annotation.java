package uk.org.taverna.scufl2.api.annotation;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import uk.org.taverna.scufl2.api.common.AbstractNamed;
import uk.org.taverna.scufl2.api.common.Child;
import uk.org.taverna.scufl2.api.common.Named;
import uk.org.taverna.scufl2.api.common.Visitor;
import uk.org.taverna.scufl2.api.common.WorkflowBean;
import uk.org.taverna.scufl2.api.container.WorkflowBundle;
import uk.org.taverna.scufl2.api.property.PropertyObject;

/**
 * An annotation of a WorkflowBean.
 * <p>
 * Modelled after http://openannotation.org/spec/core/20120509
 * 
 * @author Stian Soiland-Reyes
 *
 */
public class Annotation extends AbstractNamed implements Named, Child<WorkflowBundle> {
	private Calendar annotatedAt;
	private URI annotatedBy;
	private List<PropertyObject> bodyStatements = new ArrayList<PropertyObject>();
	private Calendar serializedAt = new GregorianCalendar();
	private URI serializedBy;
	private WorkflowBean target;
	private WorkflowBundle parent;
	private URI body;
	
	
	public Annotation(WorkflowBean target) {
		setTarget(target);
	}

	public Annotation() {
	}
	
	@Override
	public boolean accept(Visitor visitor) {
		visitor.visitEnter(this);
		for (WorkflowBean b : getBodyStatements()) {
			if (! b.accept(visitor)) {
				break;
			}
		}
		return visitor.visitLeave(this);		
	}
	public Calendar getAnnotatedAt() {
		return annotatedAt;
	}
	public URI getAnnotatedBy() {
		return annotatedBy;
	}
	public List<PropertyObject> getBodyStatements() {
		return bodyStatements;
	}
	public Calendar getSerializedAt() {
		return serializedAt;
	}
	public URI getSerializedBy() {
		return serializedBy;
	}
	public WorkflowBean getTarget() {
		if (target == null) {
			return this;
		}
		return target;
	}
	public void setAnnotatedAt(Calendar annotatedAt) {
		this.annotatedAt = annotatedAt;
	}
	public void setAnnotatedBy(URI annotatedBy) {
		this.annotatedBy = annotatedBy;
	}
	public void setBodyStatements(List<PropertyObject> bodyStatements) {
		if (bodyStatements == null) { 
			throw new NullPointerException("bodyStatements can't be null");
		}
		this.bodyStatements = bodyStatements;
	}

	public void setSerializedAt(Calendar serializedAt) {
		this.serializedAt = serializedAt;
	}
	public void setSerializedBy(URI serializedBy) {
		this.serializedBy = serializedBy;
	}
	public void setTarget(WorkflowBean target) {
		if (target == null) {
			throw new NullPointerException("Target can't be null");
		}
		this.target = target;
	}

	@Override
	public WorkflowBundle getParent() {
		return this.parent;
	}

	@Override
	public void setParent(WorkflowBundle parent) {
		if (this.parent != null && this.parent != parent) {
			this.parent.getAnnotations().remove(this);
		}
		this.parent = parent;
		if (parent != null) {
			parent.getAnnotations().add(this);
		}

	}

	public URI getBody() {
		return body;
	}

	public void setBody(URI body) {
		this.body = body;
	}

	@Override
	protected void cloneInto(WorkflowBean clone, Cloning cloning) {
		super.cloneInto(clone, cloning);
		Annotation cloneAnnotation = (Annotation)clone;
		if (getAnnotatedAt() != null) {
			cloneAnnotation.setAnnotatedAt((Calendar) getAnnotatedAt().clone());
		}
		cloneAnnotation.setAnnotatedBy(getAnnotatedBy());
		cloneAnnotation.setBody(getBody());
		for (PropertyObject statement : getBodyStatements()) {
			cloneAnnotation.getBodyStatements().add(cloning.cloneIfNotInCache(statement));
		}		
		if (getSerializedAt() != null) {
			cloneAnnotation.setSerializedAt((Calendar) getSerializedAt().clone());
		}
		cloneAnnotation.setSerializedBy(getSerializedBy());
		cloneAnnotation.setTarget(cloning.cloneOrOriginal(getTarget()));		
	}
	
}
