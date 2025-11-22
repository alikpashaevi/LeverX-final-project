package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Comment;
import alik.leverxfinalproject.model.CommentDTO;
import alik.leverxfinalproject.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser getAppUserByEmail(String email);
    boolean existsByEmail(String email);
    AppUser findByEmail(String email);


    @Query("SELECT u FROM AppUser u WHERE u.isVerified = false")
    Page<AppUser> findUnverifiedUsers(Pageable pageable);

    @Query("""
    SELECT new alik.leverxfinalproject.model.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.appUser.id = :userId
    AND c.isApproved = true
""")
    Page<CommentDTO> findCommentsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new alik.leverxfinalproject.model.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.id = :id
    AND c.appUser.id = :userId
    AND c.isApproved = true
""")
    CommentDTO findCommentById(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT c FROM Comment c WHERE c.id = :id AND c.appUser.id = c.appUser.id")
    Comment findCommentEntityById(@Param("id") Long id, @Param("userId") Long userId);

    @Query("""
        SELECT new alik.leverxfinalproject.model.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.isApproved = false
""")
    Page<CommentDTO> findUnapprovedComments(Pageable pageable);

    @Query("""
        SELECT new alik.leverxfinalproject.model.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.id = :id
    AND c.isApproved = false
""")
    CommentDTO findUnapprovedComment(@Param("id") Long id);

    @Query("""
    SELECT COUNT(c) > 0
    FROM Comment c
    WHERE c.appUser.id = :userId
      AND c.authorId = :authorId
""")
    boolean authorHasCommented(@Param("userId") Long userId,
                               @Param("authorId") String authorId);

    @Query("SELECT c FROM Comment c WHERE c.authorId = :authorId AND c.appUser.id = :userId")
    Comment findCommentByAuthorIdAndAppUserId(@Param("userId") Long userId,
                                              @Param("authorId") String authorId);

    @Query("""
    SELECT new alik.leverxfinalproject.model.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c)
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    WHERE u.id = :id
    GROUP BY u.id, u.firstName, u.lastName, u.email
""")
    UserDTO findUserWithRating(@Param("id") long id);

    @Query("""
    SELECT new alik.leverxfinalproject.model.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c)
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    GROUP BY u.id, u.firstName, u.lastName, u.email
""")
    Page<UserDTO> findAllUsersWithRatings(Pageable pageable);

//    Page<CommentDTO> findCommentsById(Long userId, Pageable pageable);
//
//    CommentDTO findCommentById(Long id);
}
