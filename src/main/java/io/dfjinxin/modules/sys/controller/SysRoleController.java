/**
 * 2019 东方金信
 *
 *
 *
 *
 */

package io.dfjinxin.modules.sys.controller;

import io.dfjinxin.common.annotation.SysLog;
import io.dfjinxin.common.utils.Constant;
import io.dfjinxin.common.utils.PageUtils;
import io.dfjinxin.common.utils.R;
import io.dfjinxin.common.validator.ValidatorUtils;
import io.dfjinxin.modules.sys.entity.SysRoleEntity;
import io.dfjinxin.modules.sys.service.SysRoleMenuService;
import io.dfjinxin.modules.sys.service.SysRoleService;
import io.dfjinxin.common.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
@Api(tags = "SysRoleController", description = "角色管理")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 所有角色列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:role:list")
	@ApiOperation("获取角色信息")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		PageUtils page = sysRoleService.queryPage(params);

		return R.ok().put("page", page);
	}




	/**
	 * 删除角色
	 */
	@PostMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	@ApiOperation("删除角色")
	public R delete(@RequestBody Map<String,Object> roleIds){
		ArrayList<Integer> roles= (ArrayList<Integer>) roleIds.get("roleIds");
		sysRoleService.deleteBatch(roles);

		return R.ok();
	}

	/**
	 * 角色下拉框
	 */
	@ApiOperation("获取角色下拉框信息")
	@GetMapping("/getRole")
	public R getRole(){
		List<Map<String,Object>> role=	sysRoleService.getRole();
		return R.ok().put("roles",role);
	}

	/**
	 * 获取角色权限
	 */
	@ApiOperation("获取角色的权限信息")
	@GetMapping("/rolePerm")
	@RequiresPermissions("sys:role:rolePerm")
	public R getRolePerm(String roleId){
	R perm=	sysRoleService.rolePerm(roleId);
	return perm;
	}

	/**
	 * 保存或更新
	 */
	@PostMapping("/info")
	@RequiresPermissions("sys:role:info")
	@ApiOperation("新增或者修改角色")
	public R addOrUpdate(@RequestBody SysRoleEntity role){
		sysRoleService.addOrUpdate(role);
		return R.ok();
	}

	@GetMapping("/checkRolePerm")
	@RequiresPermissions("sys:role:checkRolePerm")
	@ApiOperation("新增或者修改角色对权限进行验证")
	public R checkPerm(@RequestBody SysRoleEntity role){
		return sysRoleService.checkPerm(role);
	}

	@GetMapping("/checkDeleteInfo")
	@RequiresPermissions("sys:role:checkDeleteInfo")
	@ApiOperation("删除角色时进行验证")
	public R checkPermInfo(@RequestBody Map<String,Object> roleIds){
		ArrayList<Integer> roles= (ArrayList<Integer>) roleIds.get("roleIds");
		return sysRoleService.checkPermInfo(roles);
	}

}
