package com.project.workspace.domain.repository;

import com.project.workspace.domain.vo.StoryLikeVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryLikeRepository extends JpaRepository<StoryLikeVO,Long> {
}