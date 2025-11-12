package com.fitness.activity_service.service;


import com.fitness.activity_service.dtos.ActivityRequest;
import com.fitness.activity_service.dtos.ActivityResponse;
import com.fitness.activity_service.model.Activity;
import com.fitness.activity_service.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserValidationService userValidationService;

    public ActivityResponse trackActivity(ActivityRequest request) {

       boolean isValidUser=userValidationService.validateUser(request.getUserId());
       if(!isValidUser){
           throw  new RuntimeException("Invalid User: "+ request.getUserId());
       }

        Activity activity=Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity =activityRepository.save(activity);
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity) {
        ActivityResponse response=new ActivityResponse();
       response.setId(savedActivity.getId());
       response.setUserId(savedActivity.getUserId());
       response.setType(savedActivity.getType());
       response.setDuration(savedActivity.getDuration());
       response.setCaloriesBurned(savedActivity.getCaloriesBurned());
       response.setStartTime(savedActivity.getStartTime());
       response.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
       response.setCreatedAt(savedActivity.getCreatedAt());
       response.setUpdatedAt(savedActivity.getUpdatedAt());
       return response;
    }
}
