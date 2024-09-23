package com.underthekey.sentenceapi.domain;

import java.util.ArrayList;
import java.util.List;

import com.underthekey.sentenceapi.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category extends BaseEntity {
	@NonNull
	private String language;

	@NonNull
	private String type;

	@NonNull
	private String sort;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<Sentence> sentences = new ArrayList<>();
}
