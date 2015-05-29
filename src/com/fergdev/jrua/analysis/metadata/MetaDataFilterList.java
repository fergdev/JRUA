package nz.ac.massey.buto.analysis.metadata;

import java.util.ArrayList;
import java.util.List;

public class MetaDataFilterList {

	public static List<String> getDefaultFilterList(){
		
		List<String> filterList = new ArrayList<String>();
		filterList.add("stdout");
		filterList.add("runtime_classPath");
		filterList.add("runtime_name");
		filterList.add("runtime_systemProperties_sun");
		filterList.add("runtime_startTime");
		filterList.add("runtime_systemProperties_java.class.path");
		filterList.add("runtime_uptime");
		filterList.add("overhead_metadata");
		filterList.add("runtime_systemProperties_sun.java.command");
		
		filterList.add("nz.ac.massey.cs.sdc.tutorial6.concurrent:type=Production,id=1_MaxBufferSize");
		
		extendList(filterList);
		
		
		
		return filterList;
	}
	
	private static void extendList(List<String> filterList){
		filterList.add("runtime_systemProperties_java.vm.vendor");
		filterList.add("runtime_systemProperties_java.specification.version");
		filterList.add("runtime_systemProperties_java.vm.specification.name");
		filterList.add("runtime_systemProperties_path.separator");
		filterList.add("runtime_systemProperties_java.vm.info");
		filterList.add("runtime_systemProperties_user.country");
		filterList.add("runtime_systemProperties_java.specification.name");
		filterList.add("runtime_systemProperties_java.library.path");
		filterList.add("memoryPool_CodeCache_type");
		filterList.add("runtime_systemProperties_java.awt.graphicsenv");
		filterList.add("runtime_systemProperties_user.home");
		filterList.add("runtime_vmVendor");
		filterList.add("runtime_systemProperties_java.version");
		filterList.add("runtime_systemProperties_java.endorsed.dirs");
		filterList.add("runtime_systemProperties_user.dir");
		filterList.add("runtime_systemProperties_user.variant");
		filterList.add("runtime_systemProperties_java.vm.name");
		filterList.add("runtime_systemProperties_java.vm.specification.vendor");
		filterList.add("runtime_specName");
		filterList.add("runtime_systemProperties_sun.cpu.isalist");
		filterList.add("runtime_systemProperties_user.script");
		filterList.add("memoryPool_PSSurvivorSpace_managersNames");
		filterList.add("runtime_systemProperties_sun.boot.library.path");
		filterList.add("runtime_systemProperties_java.vm.version");
		filterList.add("runtime_bootClassPath");
		filterList.add("runtime_systemProperties_sun.java.launcher");
		filterList.add("os_name");
		filterList.add("runtime_systemProperties_file.encoding");
		filterList.add("os_architecture");
		filterList.add("runtime_systemProperties_sun.boot.class.path");
		filterList.add("memoryPool_PSOldGen_type");
		filterList.add("runtime_vmName");
		filterList.add("runtime_systemProperties_user.timezone");
		filterList.add("runtime_systemProperties_java.rmi.server.randomIDs");
		filterList.add("runtime_systemProperties_java.specification.vendor");
		filterList.add("runtime_specVersion");
		filterList.add("memoryPool_PSEdenSpace_type");
		filterList.add("jit_name");
		filterList.add("memoryPool_PSPermGen_managersNames");
		filterList.add("runtime_systemProperties_java.vendor.url.bug");
		filterList.add("runtime_systemProperties_os.arch");
		filterList.add("runtime_systemProperties_java.runtime.name");
		filterList.add("memoryPool_PSPermGen_type");
		filterList.add("memoryPool_PSSurvivorSpace_type");
		filterList.add("runtime_systemProperties_sun.arch.data.model");
		filterList.add("runtime_systemProperties_file.separator");
		filterList.add("runtime_systemProperties_sun.io.unicode.encoding");
		filterList.add("runtime_systemProperties_awt.toolkit");
		filterList.add("runtime_systemProperties_java.ext.dirs");
		filterList.add("runtime_inputArguements");
		filterList.add("runtime_systemProperties_java.io.tmpdir");
		filterList.add("runtime_managementSpecVersion");
		filterList.add("runtime_systemProperties_java.runtime.version");
		filterList.add("runtime_systemProperties_user.name");
		filterList.add("runtime_systemProperties_file.encoding.pkg");
		filterList.add("runtime_systemProperties_sun.jnu.encoding");
		filterList.add("runtime_systemProperties_java.class.version");
		filterList.add("runtime_systemProperties_sun.cpu.endian");
		filterList.add("runtime_systemProperties_sun.desktop");
		filterList.add("runtime_systemProperties_sun.management.compiler");
		filterList.add("runtime_systemProperties_user.language");
		filterList.add("os_systemLoadAverage");
		filterList.add("runtime_specVendor");
		filterList.add("runtime_systemProperties_line.separator");
		filterList.add("runtime_systemProperties_java.awt.printerjob");
		filterList.add("runtime_systemProperties_java.vendor.url");
		filterList.add("memoryPool_PSEdenSpace_managersNames");
		filterList.add("runtime_systemProperties_os.name");
		filterList.add("runtime_systemProperties_java.vendor");
		filterList.add("runtime_systemProperties_sun.os.patch.level");
		filterList.add("memoryPool_CodeCache_managersNames");
		filterList.add("os_available_processors");
		filterList.add("os_version");
		filterList.add("runtime_systemProperties_java.home");
		filterList.add("runtime_systemProperties_os.version");
		filterList.add("memoryPool_PSOldGen_managersNames");
		filterList.add("runtime_systemProperties_java.vm.specification.version");
		filterList.add("runtime_libraryPath");
	}
}
