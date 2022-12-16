package org.catcom.classreserver.controller;

import org.catcom.classreserver.model.building.Building;
import org.catcom.classreserver.model.building.BuildingRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class BuildingController
{
    @Autowired
    private BuildingRepos buildingRepos;

    @GetMapping("/buildings")
    @ResponseBody List<Building> getAllBuilding()
    {
        return buildingRepos.findAll();
    }

    @GetMapping("/buildings/{id}")
    @ResponseBody Building getBuildingById(@PathVariable Integer id)
    {
        return buildingRepos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Building not found"));
    }

}
