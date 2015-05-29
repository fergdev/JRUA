package nz.ac.massey.buto.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the different ButoProperties.
 * 
 * @author fahewson
 *
 */
public final class ButoPropertyManager {

	/**
	 * Prevents instantiation.
	 */
	private ButoPropertyManager(){}
	
	
	/**
	 * Map to lookup ButoProperty objects using their keys.
	 */
	private static Map<String, ButoProperty> keyLookupList;
	
	/**
	 * Map to lookup ButoProperty objects using their names.
	 */
	private static Map<String, ButoProperty> nameLookupList;
	
	static{
		keyLookupList = new HashMap<String, ButoProperty>();
		nameLookupList = new HashMap<String, ButoProperty>();
		
		keyLookupList.put("memory_heap_used", 			new ButoProperty("memory_heap_used", 			"Heap Memory Used", 			"bytes", "java.lang.Long",	0, Long.MAX_VALUE ));
		keyLookupList.put("memory_heap_committed", 		new ButoProperty("memory_heap_committed", 		"Heap Memory Committed", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("memory_nonHeap_used", 		new ButoProperty("memory_nonHeap_used", 		"Non-Heap Memory Used", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("memory_nonHeap_committed", 	new ButoProperty("memory_nonHeap_committed", 	"Non-Heap Memory Committed", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("thread_threadCount", 			new ButoProperty("thread_threadCount", 				"Thread Count", 				"threads", "java.lang.Integer",	0, Integer.MAX_VALUE  ));
		keyLookupList.put("thread_daemonThreadCount", 		new ButoProperty("thread_daemonThreadCount", 		"Daemon Thread Count", 			"threads", "java.lang.Integer",	0, Integer.MAX_VALUE  ));
		keyLookupList.put("thread_peakThreadCount", 		new ButoProperty("thread_peakThreadCount", 			"Peak Thread Count", 			"threads", "java.lang.Integer",	0, Integer.MAX_VALUE  ));
		keyLookupList.put("thread_totalStartedThreadCount", new ButoProperty("thread_totalStartedThreadCount", 	"Total Started Thread Count", 	"threads", "java.lang.Integer",	0, Integer.MAX_VALUE  ));
		
		keyLookupList.put("overhead_bufferpool", 	new ButoProperty("overhead_bufferpool", 	"Buffer Pool Collection Overhead", 		"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_classLoading", 	new ButoProperty("overhead_classLoading", 	"Class Loading Collection Overhead", 	"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_compilation", 	new ButoProperty("overhead_compilation", 	"Compilaiton Collection Overhead", 		"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_gc", 			new ButoProperty("overhead_gc", 			"GC Collection Overhead", 				"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_memory", 		new ButoProperty("overhead_memory", 		"Memory Collection Overhead", 			"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_memorypool", 	new ButoProperty("overhead_memorypool", 	"Memory Pool Collection Overhead", 		"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_sunos", 		new ButoProperty("overhead_sunos", 			"Sunos Collection Overhead", 			"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_thread", 		new ButoProperty("overhead_thread", 		"Thread Collection Overhead", 			"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_total", 		new ButoProperty("overhead_total", 			"Collection Overhead", 					"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("gc_PS_MarkSweep_collectionCount", 	new ButoProperty("gc_PS_MarkSweep_collectionCount", 		"Mark Sweep GC Collection count", 	"events", 	"java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("gc_PS_MarkSweep_collectionTime", 	new ButoProperty("gc_PS_MarkSweep_collectionTime", 			"Mark Sweep GC Collection Time", 	"ms", 		"java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("gc_PS_Scavenge_collectionCount", 	new ButoProperty("gc_PS_Scavenge_collectionCount", 			"Scavenge GC Collection Count", 	"events", 	"java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("gc_PS_Scavenge_collectionTime", 		new ButoProperty("gc_PS_Scavenge_collectionTime", 			"Scavenge GC Collection Time",	 	"ms", 		"java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("bufferPool_direct_count", 			new ButoProperty("bufferPool_direct_count", 			"Buffer Pool Direct Count", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("bufferPool_direct_memoryUsed", 		new ButoProperty("bufferPool_direct_memoryUsed", 		"Buffer Pool Direct Memory Used", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("bufferPool_direct_totalCapacity",	new ButoProperty("bufferPool_direct_totalCapacity", 	"Buffer Pool Direct Count", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("bufferPool_mapped_count", 			new ButoProperty("bufferPool_mapped_count", 			"Buffer Pool Mapped Count", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("bufferPool_mapped_memoryUsed", 		new ButoProperty("bufferPool_mapped_memoryUsed", 		"Buffer Pool Mapped Memory Used", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("bufferPool_mapped_totalCapacity", 	new ButoProperty("bufferPool_mapped_totalCapacity", 	"Buffer Pool Mapped Count", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("classLoading_totalLoaded", 		new ButoProperty("classLoading_totalLoaded", 		"Total Loaded Class count", 	"classes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("classLoading_unloadedCount", 	new ButoProperty("classLoading_unloadedCount", 		"Unloaded Class Count", 		"classes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("compilation_time", 				new ButoProperty("compilation_time", 				"Compilation Time", 			"classes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("memory_pendingFinilizationCount", new ButoProperty("memory_pendingFinilizationCount", "Object Pending Finalization Count", "objects", "java.lang.Integer", 0, Integer.MAX_VALUE));
		
		keyLookupList.put("memoryPool_PS_Eden_Space_usage_used", 	new ButoProperty("memoryPool_PS_Eden_Space_usage_used", "Eden Space Memory Used", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("memoryPool_PS_Old_Gen_usage_used", 		new ButoProperty("memoryPool_PS_Old_Gen_usage_used", 	"Old Generation Memory Used", 		"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("memoryPool_PS_Perm_Gen_usage_used", 		new ButoProperty("memoryPool_PS_Perm_Gen_usage_used", 	"Perm Generation Memory Used", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		
		keyLookupList.put("memoryPool_PS_Eden_Space_usage_committed", 	new ButoProperty("memoryPool_PS_Eden_Space_usage_committed", 	"Eden Space Memory Committed", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("memoryPool_PS_Old_Gen_usage_committed", 		new ButoProperty("memoryPool_PS_Old_Gen_usage_committed", 		"Old Gen Memory Committed", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("memoryPool_PS_Perm_Gen_usage_committed", 	new ButoProperty("memoryPool_PS_Perm_Gen_usage_committed", 		"Perm Gen Memory Committed", 	"bytes", "java.lang.Long",	0, Long.MAX_VALUE  ));
	
		keyLookupList.put("overhead_total", 		new ButoProperty("overhead_total", 			"Overhead Total", 			"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_gc", 			new ButoProperty("overhead_gc", 			"Overhead GC", 				"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_memorypool", 	new ButoProperty("overhead_memorypool", 	"Overhead Memory Pool", 	"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_customMBeans", 	new ButoProperty("overhead_customMBeans", 	"Overhead Custom MBean",	"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_thread", 		new ButoProperty("overhead_thread", 		"Overhead Thread", 			"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_compilation", 	new ButoProperty("overhead_compilation", 	"Overhead Compilation", 	"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_classLoading", 	new ButoProperty("overhead_classLoading", 	"Overhead Class Loading",	"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
		keyLookupList.put("overhead_memory", 		new ButoProperty("overhead_memory", 		"Overhead Memory", 			"ms", "java.lang.Long",	0, Long.MAX_VALUE  ));
	}
	
	/**
	 * Gets the ButoProperty object associated with the provided key.
	 * 
	 * @param key The key of the ButoProperty to retrieve.
	 * @return The ButoProperty object.
	 */
	public static ButoProperty getButoProperty(String key){
		return keyLookupList.get(key);
	}

	/**
	 * Gets the ButoProperty object associated with the provided name.
	 * 
	 * @param name The name of the ButoProperty.
	 * @return The ButoProperty object.
	 */
	public static ButoProperty getButoPropertyFromName(String name){
		return nameLookupList.get(name);
	}
}
