package com.github.richardwilly98.esdms;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.richardwilly98.esdms.api.Document;
import com.github.richardwilly98.esdms.api.File;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

public class DocumentImpl extends SecuredItemImpl implements Document {

	private static final long serialVersionUID = 1L;
	private static final Set<String> readOnlyAttributes = ImmutableSet.of(
			AUTHOR, CREATION_DATE, MODIFIED_DATE, STATUS, LOCKED_BY);

	private String versionId;
	@JsonProperty("file")
	private File file;
	private Set<String> tags;

	public static class Builder extends
			SecuredItemImpl.Builder<DocumentImpl.Builder> {

		private String versionId;
		private File file;
		private Set<String> tags;

		public Builder versionId(String versionId) {
			this.versionId = versionId;
			return getThis();
		}

		public Builder file(File file) {
			this.file = file;
			return getThis();
		}

		public Builder tags(Set<String> tags) {
			this.tags = tags;
			return getThis();
		}

		@Override
		protected Builder getThis() {
			return this;
		}

		public DocumentImpl build() {
			return new DocumentImpl(this);
		}
	}

	DocumentImpl() {
		this(null);
	}

	protected DocumentImpl(Builder builder) {
		super(builder);
		if (builder != null) {
			if (Strings.isNullOrEmpty(builder.versionId)) {
				this.versionId = "1";
			} else {
				this.versionId = builder.versionId;
			}
			this.file = builder.file;
			this.tags = builder.tags;
		}
		readOnlyAttributeKeys = readOnlyAttributes;
	}

	/*
	 * Method used to deserialize attributes Map
	 */
	@JsonProperty("attributes")
	private void deserialize(Map<String, Object> attributes) {
		if (!attributes.containsKey(DocumentImpl.STATUS)) {
			attributes.put(DocumentImpl.STATUS,
					DocumentImpl.DocumentStatus.AVAILABLE.getStatusCode());
		}
		this.attributes = attributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.richardwilly98.api.IDocument#getFile()
	 */
	@Override
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.richardwilly98.api.IDocument#getVersionId()
	 */
	@Override
	public String getVersionId() {
		return versionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.github.richardwilly98.api.IDocument#getTags()
	 */
	@Override
	public Set<String> getTags() {
		return tags;
	}

	void setTags(Set<String> tags) {
		this.tags = tags;
	}

	@Override
	public void addTag(String tag) {
		if (tags == null) {
			tags = newHashSet();
		}
		tags.add(tag);
	}

	@Override
	public void removeTag(String tag) {
		checkNotNull(tag);
		if (tags != null) {
			if (tags.contains(tag)) {
				tags.remove(tag);
			}
		}
	}

}
