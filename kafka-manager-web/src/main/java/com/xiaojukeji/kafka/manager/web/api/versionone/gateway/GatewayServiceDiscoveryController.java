package com.xiaojukeji.kafka.manager.web.api.versionone.gateway;

import com.alibaba.fastjson.JSON;
import com.xiaojukeji.kafka.manager.common.annotations.ApiLevel;
import com.xiaojukeji.kafka.manager.common.bizenum.gateway.GatewayConfigKeyEnum;
import com.xiaojukeji.kafka.manager.common.constant.ApiLevelContent;
import com.xiaojukeji.kafka.manager.common.entity.DeprecatedResponseResult;
import com.xiaojukeji.kafka.manager.common.entity.ao.gateway.*;
import com.xiaojukeji.kafka.manager.common.entity.pojo.gateway.GatewayConfigDO;
import com.xiaojukeji.kafka.manager.common.entity.vo.gateway.GatewayConfigVO;
import com.xiaojukeji.kafka.manager.common.utils.ListUtils;
import com.xiaojukeji.kafka.manager.common.utils.ValidateUtils;
import com.xiaojukeji.kafka.manager.service.service.gateway.GatewayConfigService;
import com.xiaojukeji.kafka.manager.common.constant.ApiPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zengqiao
 * @date 20/7/27
 */
@Api(tags = "GATEWAY-服务发现相关接口(REST)")
@RestController
@RequestMapping(ApiPrefix.GATEWAY_API_V1_PREFIX)
public class GatewayServiceDiscoveryController {
    @Autowired
    private GatewayConfigService gatewayConfigService;

    @ApiLevel(level = ApiLevelContent.LEVEL_VIP_1)
    @ApiOperation(value = "获取指定集群服务地址", notes = "")
    @RequestMapping(value = "discovery/address", method = RequestMethod.GET)
    @ResponseBody
    public String getKafkaBootstrapServer(@RequestParam("clusterId") Long clusterId) {
        if (ValidateUtils.isNull(clusterId)) {
            return "";
        }
        GatewayConfigDO configDO = gatewayConfigService.getByTypeAndName(
                GatewayConfigKeyEnum.SD_CLUSTER_ID.getConfigType(),
                String.valueOf(clusterId)
        );
        if (ValidateUtils.isNull(configDO)) {
            return "";
        }
        return configDO.getValue();
    }

    @ApiLevel(level = ApiLevelContent.LEVEL_VIP_1)
    @ApiOperation(value = "获取集群服务地址", notes = "")
    @RequestMapping(value = "discovery/init", method = RequestMethod.GET)
    @ResponseBody
    public DeprecatedResponseResult<String> getAllKafkaBootstrapServers() {
        KafkaBootstrapServerConfig config =
                gatewayConfigService.getKafkaBootstrapServersConfig(Long.MIN_VALUE);
        if (ValidateUtils.isNull(config) || ValidateUtils.isNull(config.getClusterIdBootstrapServersMap())) {
            return DeprecatedResponseResult.failure("call init kafka bootstrap servers failed");
        }
        if (config.getClusterIdBootstrapServersMap().isEmpty()) {
            return DeprecatedResponseResult.success();
        }
        return DeprecatedResponseResult.success(JSON.toJSONString(config.getClusterIdBootstrapServersMap()));
    }

    @ApiLevel(level = ApiLevelContent.LEVEL_IMPORTANT_2)
    @ApiOperation(value = "获取集群服务地址", notes = "")
    @RequestMapping(value = "discovery/update", method = RequestMethod.GET)
    @ResponseBody
    public DeprecatedResponseResult getBootstrapServersIfNeeded(@RequestParam("versionNumber") long versionNumber) {
        KafkaBootstrapServerConfig config =
                gatewayConfigService.getKafkaBootstrapServersConfig(versionNumber);
        if (ValidateUtils.isNull(config) || ValidateUtils.isNull(config.getClusterIdBootstrapServersMap())) {
            return DeprecatedResponseResult.failure("call update kafka bootstrap servers failed");
        }

        if (config.getClusterIdBootstrapServersMap().isEmpty()) {
            return DeprecatedResponseResult.success();
        }
        return DeprecatedResponseResult.success(JSON.toJSONString(new GatewayConfigVO(
                String.valueOf(config.getVersion()),
                JSON.toJSONString(config.getClusterIdBootstrapServersMap())
        )));
    }

    @ApiLevel(level = ApiLevelContent.LEVEL_IMPORTANT_2)
    @ApiOperation(value = "最大并发请求数", notes = "")
    @RequestMapping(value = "discovery/max-request-num", method = RequestMethod.GET)
    @ResponseBody
    public DeprecatedResponseResult getMaxRequestNum(@RequestParam("versionNumber") long versionNumber) {
        RequestQueueConfig config = gatewayConfigService.getRequestQueueConfig(versionNumber);
        if (ValidateUtils.isNull(config)) {
            return DeprecatedResponseResult.failure("call get request queue size config failed");
        }
        if (ValidateUtils.isNull(config.getMaxRequestQueueSize())) {
            return DeprecatedResponseResult.success();
        }
        return DeprecatedResponseResult.success(JSON.toJSONString(
                new GatewayConfigVO(
                        String.valueOf(config.getVersion()),
                        String.valueOf(config.getMaxRequestQueueSize())
                )
        ));
    }

    @ApiLevel(level = ApiLevelContent.LEVEL_IMPORTANT_2)
    @ApiOperation(value = "最大APP请求速率", notes = "")
    @RequestMapping(value = "discovery/appId-rate", method = RequestMethod.GET)
    @ResponseBody
    public DeprecatedResponseResult getAppIdRate(@RequestParam("versionNumber") long versionNumber) {
        AppRateConfig config = gatewayConfigService.getAppRateConfig(versionNumber);
        if (ValidateUtils.isNull(config)) {
            return DeprecatedResponseResult.failure("call get app rate config failed");
        }
        if (ValidateUtils.isNull(config.getAppRateLimit())) {
            return DeprecatedResponseResult.success();
        }
        return DeprecatedResponseResult.success(JSON.toJSONString(
                new GatewayConfigVO(
                        String.valueOf(config.getVersion()),
                        String.valueOf(config.getAppRateLimit())
                )
        ));
    }

    @ApiLevel(level = ApiLevelContent.LEVEL_IMPORTANT_2)
    @ApiOperation(value = "最大IP请求速率", notes = "")
    @RequestMapping(value = "discovery/ip-rate", method = RequestMethod.GET)
    @ResponseBody
    public DeprecatedResponseResult getIpRate(@RequestParam("versionNumber") long versionNumber) {
        IpRateConfig config = gatewayConfigService.getIpRateConfig(versionNumber);
        if (ValidateUtils.isNull(config)) {
            return DeprecatedResponseResult.failure("call get ip rate config failed");
        }
        if (ValidateUtils.isNull(config.getIpRateLimit())) {
            return DeprecatedResponseResult.success();
        }
        return DeprecatedResponseResult.success(JSON.toJSONString(
                new GatewayConfigVO(
                        String.valueOf(config.getVersion()),
                        String.valueOf(config.getIpRateLimit())
                )
        ));
    }

    @ApiLevel(level = ApiLevelContent.LEVEL_IMPORTANT_2)
    @ApiOperation(value = "最大SP请求速率", notes = "")
    @RequestMapping(value = "discovery/sp-limit", method = RequestMethod.GET)
    @ResponseBody
    public DeprecatedResponseResult getSpLimit(@RequestParam("versionNumber") long versionNumber) {
        SpRateConfig config =
                gatewayConfigService.getSpRateConfig(versionNumber);
        if (ValidateUtils.isNull(config) || ValidateUtils.isNull(config.getSpRateMap())) {
            return DeprecatedResponseResult.failure("call update kafka bootstrap servers failed");
        }

        if (config.getSpRateMap().isEmpty()) {
            return DeprecatedResponseResult.success();
        }

        List<String> strList = new ArrayList<>();
        for (Map.Entry<String, Long> entry: config.getSpRateMap().entrySet()) {
            strList.add(entry.getKey() + "#" + String.valueOf(entry.getValue()));
        }

        return DeprecatedResponseResult.success(JSON.toJSONString(new GatewayConfigVO(
                String.valueOf(config.getVersion()),
                ListUtils.strList2String(strList)
        )));
    }
}