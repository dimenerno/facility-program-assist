# QueryDSL Upgrade Documentation

## Overview
This document describes the upgrade from hard-coded JPQL queries to QueryDSL in the facility-assist application.

## Changes Made

### 1. Dependencies Added
Added the following dependencies to `pom.xml`:
- `querydsl-jpa`: Core QueryDSL JPA support
- `querydsl-apt`: Annotation processor for generating Q-classes

### 2. Maven Configuration
Updated the `maven-compiler-plugin` to include QueryDSL annotation processor:
```xml
<annotationProcessorPaths>
    <path>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-apt</artifactId>
        <version>${querydsl.version}</version>
    </path>
    <path>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
    </path>
</annotationProcessorPaths>
```

### 3. QueryDSL Configuration
Created `QueryDSLConfig.java` to provide `JPAQueryFactory` bean for dependency injection.

### 4. Repository Upgrades

#### DocumentRepository
- **Before**: Used `@Query` annotations with hard-coded JPQL
- **After**: Implemented `DocumentRepositoryImpl` using QueryDSL with type-safe queries
- **Methods upgraded**:
  - `findAllActiveOrderByUploadedAtDesc()`
  - `findAllActiveOrderByUploadedAtDesc(Pageable)`
  - `findByIdAndIsActiveTrue(Long)`
  - `findByUploaderIdAndIsActiveTrue(Long)`
  - `countActiveDocuments()`

#### NoticeRepository
- **Before**: Used `@Query` annotations with hard-coded JPQL
- **After**: Implemented `NoticeRepositoryImpl` using QueryDSL with type-safe queries
- **Methods upgraded**:
  - `findAllOrderByCreatedAtDesc()`
  - `findAllOrderByCreatedAtDesc(Pageable)`
  - `findTopNByOrderByCreatedAtDesc(Pageable)`
  - `findByWrittenByIdOrderByCreatedAtDesc(Long)`
  - `findByTitleContainingOrContentContainingOrderByCreatedAtDesc(String)`

### 5. Repository Pattern
Used the custom repository pattern with proper file structure:
- Created `*RepositoryCustom` interfaces defining custom methods
- Implemented `*RepositoryImpl` classes with QueryDSL logic in separate files
- Extended main repository interfaces to inherit both JPA and custom methods

**File Structure:**
- `DocumentRepository.java` - Main repository interface
- `DocumentRepositoryCustom.java` - Custom method interface
- `DocumentRepositoryImpl.java` - QueryDSL implementation
- `NoticeRepository.java` - Main repository interface  
- `NoticeRepositoryCustom.java` - Custom method interface
- `NoticeRepositoryImpl.java` - QueryDSL implementation

## Benefits of QueryDSL

1. **Type Safety**: Compile-time checking of queries prevents runtime errors
2. **IDE Support**: Better autocomplete and refactoring support
3. **Dynamic Queries**: Easy to build complex queries conditionally
4. **Maintainability**: More readable and maintainable query code
5. **Performance**: Same performance as JPQL with better tooling

## Usage Examples

### Simple Query
```java
// Before (JPQL)
@Query("SELECT d FROM Document d WHERE d.isActive = true ORDER BY d.uploadedAt DESC")
List<Document> findAllActiveOrderByUploadedAtDesc();

// After (QueryDSL)
public List<Document> findAllActiveOrderByUploadedAtDesc() {
    return queryFactory
            .selectFrom(document)
            .where(document.isActive.eq(true))
            .orderBy(document.uploadedAt.desc())
            .fetch();
}
```

### Dynamic Query
```java
// QueryDSL allows for dynamic query building
public List<Notice> searchNotices(String searchText, Long authorId) {
    BooleanBuilder builder = new BooleanBuilder();
    
    if (searchText != null && !searchText.isEmpty()) {
        String pattern = "%" + searchText + "%";
        builder.or(notice.title.likeIgnoreCase(pattern))
               .or(notice.content.likeIgnoreCase(pattern));
    }
    
    if (authorId != null) {
        builder.and(notice.writtenBy.id.eq(authorId));
    }
    
    return queryFactory
            .selectFrom(notice)
            .where(builder)
            .orderBy(notice.createdAt.desc())
            .fetch();
}
```

## Testing
Created `QueryDSLRepositoryTest.java` to verify the QueryDSL implementation works correctly with the existing functionality.

## Migration Notes
- All existing service layer code remains unchanged
- Repository method signatures are preserved
- No changes needed in controllers or DTOs
- Q-classes are generated automatically during compilation

## Next Steps
1. Run `mvn clean compile` to generate Q-classes
2. Run tests to verify functionality
3. Consider adding more dynamic query methods as needed
4. Monitor performance and optimize queries if necessary
