package alik.leverxfinalproject.repo;

import alik.leverxfinalproject.entity.AppUser;
import alik.leverxfinalproject.entity.Comment;
import alik.leverxfinalproject.model.dto.CommentDTO;
import alik.leverxfinalproject.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    AppUser getAppUserByEmail(String email);
    boolean existsByEmail(String email);
    AppUser findByEmail(String email);

    @Query("Select u FROM AppUser u WHERE u.id = :id AND u.isVerified = false")
    AppUser findUnverifiedUserEntityById(@Param("id") long id);

    @Query("""
    SELECT new alik.leverxfinalproject.model.dto.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.updatedAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.appUser.id = :userId
    AND c.isApproved = true
""")
    Page<CommentDTO> findCommentsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        SELECT new alik.leverxfinalproject.model.dto.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.updatedAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.id = :id
    AND c.appUser.id = :userId
    AND c.isApproved = true
""")
    CommentDTO findCommentById(@Param("id") Long id, @Param("userId") Long userId);

    @Query("SELECT c FROM Comment c WHERE c.id = :id AND c.appUser.id = c.appUser.id AND c.isApproved = true")
    Comment findCommentEntityById(@Param("id") Long id, @Param("userId") Long userId);

    @Query("""
        SELECT new alik.leverxfinalproject.model.dto.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.updatedAt,
        c.isApproved
    )
    FROM Comment c
    WHERE c.isApproved = false
""")
    Page<CommentDTO> findUnapprovedComments(Pageable pageable);

    @Query("""
        SELECT new alik.leverxfinalproject.model.dto.CommentDTO(
        c.id,
        c.text,
        c.rating,
        c.appUser.id,
        c.authorId,
        c.createdAt,
        c.updatedAt,
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

    @Query("SELECT c FROM Comment c WHERE c.authorId = :authorId AND c.appUser.id = :userId AND c.isApproved = true")
    Comment findCommentByAuthorIdAndAppUserId(@Param("userId") Long userId,
                                              @Param("authorId") String authorId);

    @Query("""
    SELECT new alik.leverxfinalproject.model.dto.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c),
        u.createdAt
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    WHERE u.id = :id
    GROUP BY u.id, u.firstName, u.lastName, u.email
""")
    UserDTO findUserDTO(@Param("id") long id);

    @Query("""
    SELECT new alik.leverxfinalproject.model.dto.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c),
        u.createdAt
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    WHERE u.isVerified = false
    AND u.isEmailVerified = true
    GROUP BY u.id, u.firstName, u.lastName, u.email
""")
    Page<UserDTO> getUnverifiedUsers( Pageable pageable);


    @Query("""
    SELECT new alik.leverxfinalproject.model.dto.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c),
        u.createdAt
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    WHERE u.isVerified = false
    AND u.id = :id
    AND u.isEmailVerified = true
    GROUP BY u.id, u.firstName, u.lastName, u.email
""")
    UserDTO getUnverifiedUserById(@Param("id") long id);

    @Query("""
    SELECT new alik.leverxfinalproject.model.dto.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c),
        u.createdAt
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    WHERE u.isVerified = true
    GROUP BY u.id, u.firstName, u.lastName, u.email
""")
    Page<UserDTO> findAllUsersWithRatings(Pageable pageable);

    @Query("""
    SELECT new alik.leverxfinalproject.model.dto.UserDTO(
        u.id,
        u.firstName,
        u.lastName,
        u.email,
        AVG(c.rating),
        COUNT(c),
        u.createdAt
    )
    FROM AppUser u
    LEFT JOIN u.comments c
    WHERE u.isVerified = true
    GROUP BY u.id, u.firstName, u.lastName, u.email
    HAVING COUNT(c) > 0
    ORDER BY COALESCE(AVG(c.rating), 0) DESC, COUNT(c) DESC
    """)
    Page<UserDTO> findTopSellers(Pageable pageable);

    @Query("""
        SELECT new alik.leverxfinalproject.model.dto.UserDTO(
            u.id,
            u.firstName,
            u.lastName,
            u.email,
            AVG(c.rating),
            COUNT(c),
            u.createdAt
        )
        FROM AppUser u
        LEFT JOIN u.comments c
        LEFT JOIN u.gameObjects gObj
        LEFT JOIN gObj.game g
        WHERE (:gameId IS NULL OR g.id = :gameId)
        AND u.isVerified = true
        GROUP BY u.id, u.firstName, u.lastName, u.email
        HAVING COALESCE(AVG(c.rating), 0) BETWEEN :minRating AND :maxRating
        ORDER BY COALESCE(AVG(c.rating), 0) DESC, COUNT(c) DESC
    """)
    Page<UserDTO> findUsersByGameAndRatingRange(
            @Param("gameId") Long gameId,
            @Param("minRating") double minRating,
            @Param("maxRating") double maxRating,
            Pageable pageable
    );

//    Page<CommentDTO> findCommentsById(Long userId, Pageable pageable);
//
//    CommentDTO findCommentById(Long id);
}
