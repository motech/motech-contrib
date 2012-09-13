package org.motechproject.diagnostics.repository;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.motechproject.diagnostics.model.BatchJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class AllBatchJobs {

    @Value("${spring.batch.api.end.point}")
    private String springBatchServiceEndPoint;

    @Autowired
    @Qualifier("springBatchDiagnosticsHttpClientFactory")
    private CommonsClientHttpRequestFactory httpClientFactory;

    public boolean canFetchData() {
        return !"${spring.batch.api.end.point}".equals(springBatchServiceEndPoint);
    }

    public List<BatchJob> fetchAll() {
        return fetchJobs(fetchRegistrationNames());
    }

    private Set<String> fetchRegistrationNames() {
        JobsTemplate template = new JobsTemplate(springBatchServiceEndPoint, httpClientFactory);
        return template.allJobs().getJobs().getRegistrations().keySet();
    }

    private List<BatchJob> fetchJobs(Set<String> jobNames) {
        JobsTemplate template = new JobsTemplate(springBatchServiceEndPoint, httpClientFactory);
        List<BatchJob> batchJobs = new ArrayList<BatchJob>();
        for (String name : jobNames) {
            batchJobs.add(template.jobDetails(name));
        }
        return batchJobs;
    }


    private static class JobsTemplate extends RestTemplate {

        private String serviceEndPoint;

        public JobsTemplate(String serviceEndPoint, CommonsClientHttpRequestFactory httpClientFactory) {
            super(httpClientFactory);
            this.serviceEndPoint = serviceEndPoint;
            HttpMessageConverter<?> converter = new MappingJacksonHttpMessageConverter();
            List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
            converters.add(converter);
            setMessageConverters(converters);

        }

        public BatchResponse allJobs() {
            return getForEntity(serviceEndPoint + "/jobs.json", BatchResponse.class).getBody();
        }

        public BatchJob jobDetails(String jobName) {
            return getForEntity(serviceEndPoint + "/jobs/" + jobName + ".json", BatchJobResponse.class).getBody().getJob();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BatchResponse {

        private BatchJobs jobs;

        public BatchJobs getJobs() {
            return jobs;
        }

        public void setJobs(BatchJobs jobs) {
            this.jobs = jobs;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        private static class BatchJobs {

            private Map<String, Object> registrations;

            public Map<String, Object> getRegistrations() {
                return registrations;
            }

            public void setRegistrations(Map<String, Object> registrations) {
                this.registrations = registrations;
            }
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class BatchJobResponse {

        private BatchJob job;

        public BatchJob getJob() {
            return job;
        }

        public void setJob(BatchJob job) {
            this.job = job;
        }
    }
}
