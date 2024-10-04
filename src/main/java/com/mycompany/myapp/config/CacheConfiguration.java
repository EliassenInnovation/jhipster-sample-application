package com.mycompany.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.mycompany.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.mycompany.myapp.domain.User.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Authority.class.getName());
            createCache(cm, com.mycompany.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.mycompany.myapp.domain.AllergenMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ApplicationValue.class.getName());
            createCache(cm, com.mycompany.myapp.domain.BlockReportPost.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Category.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityComment.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityLikeMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityPost.class.getName());
            createCache(cm, com.mycompany.myapp.domain.CommunityPostTransactions.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Distributor.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ErrorLog.class.getName());
            createCache(cm, com.mycompany.myapp.domain.H7KeywordMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.IocCategory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.LoginHistory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Manufacturer.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MonthlyNumbers.class.getName());
            createCache(cm, com.mycompany.myapp.domain.MonthMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.Notification.class.getName());
            createCache(cm, com.mycompany.myapp.domain.OneWorldSyncProduct.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PostTypes.class.getName());
            createCache(cm, com.mycompany.myapp.domain.PrivacyType.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductActivityHistory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductAllergen.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductAllergenBak.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductBeforeApprove.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductChangeHistory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductDistributorAllocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductDistrictAllocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductGtinAllocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductH7.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductH7New.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductH7Old.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductImage.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductImageBeforeApprove.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductManufacturerAllocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductsToUpdate.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ProductUpcAllocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.ReplacedProducts.class.getName());
            createCache(cm, com.mycompany.myapp.domain.RoleMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SchoolDistrict.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SetMappings.class.getName());
            createCache(cm, com.mycompany.myapp.domain.StateInfo.class.getName());
            createCache(cm, com.mycompany.myapp.domain.StorageTypes.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SubCategory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SuggestedProducts.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SupportCategory.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SupportTicketMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.SupportTicketTransaction.class.getName());
            createCache(cm, com.mycompany.myapp.domain.USDAUpdateMst.class.getName());
            createCache(cm, com.mycompany.myapp.domain.UserDistrictAllocation.class.getName());
            createCache(cm, com.mycompany.myapp.domain.UserMst.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
