package com.${group.replace("-", "")}.${name.replace("-", "")}.resources;

import com.${group.replace("-", "")}.${name.replace("-", "")}.domain.Demo;
import com.${group.replace("-", "")}.${name.replace("-", "")}.resources.assembler.DemoAssembler;
import com.${group.replace("-", "")}.${name.replace("-", "")}.resources.dto.DemoCreationRequest;
import com.${group.replace("-", "")}.${name.replace("-", "")}.resources.dto.DemoResponse;
import com.${group.replace("-", "")}.${name.replace("-", "")}.application.DemoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demos")
public class DemoController {

    private DemoService demoService;

    private DemoAssembler demoAssembler;

    @Autowired
    public DemoController(DemoService demoService, DemoAssembler demoAssembler) {
        this.demoService = demoService;
        this.demoAssembler = demoAssembler;
    }

    @GetMapping
    @ApiOperation("Get all demo items")
    public List<DemoResponse> getAllDemos() {
        final List<Demo> demos = demoService.getDemos();
        return demoAssembler.toDemoResponseList(demos);
    }

    @ApiOperation("Get demo by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "demoId", required = true, defaultValue = "1")
    })
    @RequestMapping(value = "/{demoId}", method = RequestMethod.GET, headers = "Accept=application/json")
    public DemoResponse getDemosById(@PathVariable("demoId") final Long demoId) {

        final Demo demo = demoService.getDemosById(demoId);
        return demoAssembler.toDemoResponse(demo);
    }


    @ApiOperation("Create new demo item")
    @PostMapping
    public DemoResponse createDemo(@RequestBody DemoCreationRequest request) {
        Demo demo = demoAssembler.toDomainObject(request);
        return demoAssembler.toDemoResponse(demoService.save(demo));
    }

    @ApiOperation("update demo info")
    @RequestMapping(value = "/{demoId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public void updateDemo(@PathVariable("demoId") final Long demoId,
                              @RequestBody DemoCreationRequest request){
        Demo newDemo = demoAssembler.toDomainObject(request);
        Demo oldDemo = demoService.getDemosById(demoId);
        if(newDemo.getName() != null){
            oldDemo.setName(newDemo.getName());
        }
        demoService.save(oldDemo);
    }
}
