/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.write;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.write.FileUploadDto;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UpLoadFileService {

	/**
	 * 用来缓存用户上传的文件
	 */
	@Resource
	private RedisTemplate<String, List<FileUploadDto>> redisTemplate;

	/**
	 * 图片文件上传
	 * 上传文件
	 */
	public List<FileUploadDto> upLoadFile(MultipartHttpServletRequest multiRequest, String tempId, Long fileId) throws IOException {
		List<FileUploadDto> ret = new ArrayList<>();
		//获取multiRequest 中所有的文件名
        Iterator<String> iter=multiRequest.getFileNames();
        while(iter.hasNext()){
            //一次遍历所有文件
            MultipartFile file=multiRequest.getFile(iter.next());
            if(file!=null) {
            	String filename = file.getOriginalFilename();
            	String extName = filename.substring(filename.lastIndexOf("."));
        		//拷贝文件
        		String id = fileId.toString();
        		filename = id + extName;
                String path= RSBIUtils.getUploadFilePath(multiRequest.getServletContext())+filename;
                //上传
				File targetFile = new File(path);
				file.transferTo(targetFile);

				//缓存文件
				List<FileUploadDto> files = redisTemplate.opsForValue().get(tempId);
				if(files == null || file.getSize() == 0){
					files = new ArrayList<>();
					FileUploadDto dto = new FileUploadDto(fileId, extName, file.getOriginalFilename());
					files.add(dto);
					redisTemplate.opsForValue().set(tempId, files, 30, TimeUnit.MINUTES);
				}else {
					FileUploadDto dto = new FileUploadDto(fileId, extName, file.getOriginalFilename());
					files.add(dto);
					redisTemplate.opsForValue().set(tempId, files);
				}
				ret.addAll(files);
			}
        }
        return ret;
	}

	public List<FileUploadDto> fileRemove(HttpServletRequest req, String tempId, Long fileId) {
		List<FileUploadDto> files = redisTemplate.opsForValue().get(tempId);
		int idx = -1;
		String extName = null;
		for(int i=0; i<files.size(); i++){
			FileUploadDto dto = files.get(i);
			if(dto.getName().equals(fileId)){
				idx = i;
				extName = dto.getExtName();
				break;
			}
		}
		files.remove(idx);
		redisTemplate.opsForValue().set(tempId, files);
		//移除文件
		String id = fileId.toString();
		String filename = id + extName;
		String path= RSBIUtils.getUploadFilePath(req.getServletContext())+filename;
		//上传
		boolean r = new File(path).delete();
		return files;
	}

	public List<FileUploadDto> listFiles(String tempId){
		List<FileUploadDto> ret = redisTemplate.opsForValue().get(tempId);
		redisTemplate.delete(tempId);
		return ret;
	}

	public void cacheFiles(String tempId, String json){
		List<FileUploadDto> files = new ArrayList<>();
		JSONArray arrays = JSONArray.parseArray(json);
		for(int i=0; i<arrays.size(); i++){
			JSONObject obj = arrays.getJSONObject(i);
			FileUploadDto dto = obj.toJavaObject(FileUploadDto.class);
			files.add(dto);
		}
		redisTemplate.opsForValue().set(tempId, files, 30, TimeUnit.MINUTES);
	}

	public void readFile(HttpServletRequest req, OutputStream os, String fileName, String extName){
		try {
			String path = RSBIUtils.getUploadFilePath(req.getServletContext());
			InputStream is = null;
			try {
				File f = new File(path + fileName+extName);
				if (!f.exists()) {
					return;
				}
				is = new FileInputStream(f);
				IOUtils.copy(is, os);
			} finally {
				if (is != null) is.close();
			}

		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
