// Autogenerated Jamon implementation
// /Users/panghaoyuan/Documents/src/hbase-1.1.2/hbase-server/src/main/jamon/org/apache/hadoop/hbase/tmpl/master/AssignmentManagerStatusTmpl.jamon

package org.apache.hadoop.hbase.tmpl.master;

// 20, 1
import org.apache.hadoop.hbase.HRegionInfo;
// 21, 1
import org.apache.hadoop.hbase.master.AssignmentManager;
// 22, 1
import org.apache.hadoop.hbase.master.RegionState;
// 23, 1
import org.apache.hadoop.conf.Configuration;
// 24, 1
import org.apache.hadoop.hbase.HBaseConfiguration;
// 25, 1
import org.apache.hadoop.hbase.HConstants;
// 26, 1
import java.util.Iterator;
// 27, 1
import java.util.Map;

public class AssignmentManagerStatusTmplImpl
  extends org.jamon.AbstractTemplateImpl
  implements org.apache.hadoop.hbase.tmpl.master.AssignmentManagerStatusTmpl.Intf

{
  private final AssignmentManager assignmentManager;
  private final int limit;
  protected static org.apache.hadoop.hbase.tmpl.master.AssignmentManagerStatusTmpl.ImplData __jamon_setOptionalArguments(org.apache.hadoop.hbase.tmpl.master.AssignmentManagerStatusTmpl.ImplData p_implData)
  {
    if(! p_implData.getLimit__IsNotDefault())
    {
      p_implData.setLimit(100);
    }
    return p_implData;
  }
  public AssignmentManagerStatusTmplImpl(org.jamon.TemplateManager p_templateManager, org.apache.hadoop.hbase.tmpl.master.AssignmentManagerStatusTmpl.ImplData p_implData)
  {
    super(p_templateManager, __jamon_setOptionalArguments(p_implData));
    assignmentManager = p_implData.getAssignmentManager();
    limit = p_implData.getLimit();
  }
  
  public void renderNoFlush(@SuppressWarnings({"unused","hiding"}) final java.io.Writer jamonWriter)
    throws java.io.IOException
  {
    // 33, 1
    
Map<String, RegionState> rit = assignmentManager
  .getRegionStates().getRegionsInTransition();
// process the map to find region in transition details
Configuration conf = HBaseConfiguration.create();
int ritThreshold = conf.getInt(HConstants.METRICS_RIT_STUCK_WARNING_THRESHOLD, 60000);
int numOfRITOverThreshold = 0;
long maxRITTime = Long.MIN_VALUE;
long currentTime = System.currentTimeMillis();
String regionIDForOldestRIT = ""; // avoiding null
for (Map.Entry<String, RegionState> e : rit.entrySet()) {
  long ritTime = currentTime - e.getValue().getStamp();
  if(ritTime > ritThreshold) {
     numOfRITOverThreshold++;
   }
   if(maxRITTime < ritTime) {
     maxRITTime = ritTime;
     regionIDForOldestRIT = e.getKey();
   }
}

int toRemove = rit.size() - limit;
int removed = 0;
if (toRemove > 0) {
  // getRegionsInTransition returned a copy, so we can mutate it
  for (Iterator<Map.Entry<String, RegionState>> it = rit.entrySet().iterator();
       it.hasNext() && toRemove > 0;
       ) {
    Map.Entry<String, RegionState> e = it.next();
    if (HRegionInfo.FIRST_META_REGIONINFO.getEncodedName().equals(
          e.getKey()) ||
         regionIDForOldestRIT.equals(e.getKey())) {
      // don't remove the meta & the oldest rit regions, they're too interesting!
      continue;
    } 
    it.remove();
    toRemove--;
    removed++;
  }
}


    // 77, 1
    if (!rit.isEmpty() )
    {
      // 77, 23
      jamonWriter.write("\n    <section>\n    <h2>Regions in Transition</h2>\n    <table class=\"table table-striped\">\n            <tr><th>Region</th><th>State</th><th>RIT time (ms)</th></tr>\n            ");
      // 82, 13
      for (Map.Entry<String, RegionState> entry : rit.entrySet() )
      {
        // 82, 75
        jamonWriter.write("\n            ");
        // 83, 13
        if (regionIDForOldestRIT.equals(entry.getKey()) )
        {
          // 83, 64
          jamonWriter.write("\n                    <tr BGCOLOR=\"#FE2E2E\" >\n            ");
        }
        // 85, 13
        else
        {
          // 85, 20
          jamonWriter.write("\n                    <tr>\n            ");
        }
        // 87, 19
        jamonWriter.write("\n            <td>");
        // 88, 17
        org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf(entry.getKey()), jamonWriter);
        // 88, 37
        jamonWriter.write("</td><td>");
        // 88, 46
        org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf(entry.getValue().toDescriptiveString()), jamonWriter);
        // 88, 90
        jamonWriter.write("</td>\n    <td>");
        // 89, 9
        org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf((currentTime - entry.getValue().getStamp())), jamonWriter);
        // 89, 58
        jamonWriter.write(" </td></tr>\n            ");
      }
      // 90, 20
      jamonWriter.write("\n            <tr BGCOLOR=\"#D7DF01\"> <td>Total number of Regions in Transition for more than ");
      // 91, 92
      org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf(ritThreshold), jamonWriter);
      // 91, 110
      jamonWriter.write(" milliseconds</td><td> ");
      // 91, 133
      org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf(numOfRITOverThreshold), jamonWriter);
      // 91, 160
      jamonWriter.write("</td><td></td>\n            </tr>\n    <tr> <td> Total number of Regions in Transition</td><td>");
      // 93, 61
      org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf(rit.size()), jamonWriter);
      // 93, 77
      jamonWriter.write(" </td><td></td>\n    </table>\n    ");
      // 95, 5
      if (removed > 0 )
      {
        // 95, 24
        jamonWriter.write("\n    (");
        // 96, 6
        org.jamon.escaping.Escaping.HTML.write(org.jamon.emit.StandardEmitter.valueOf(removed), jamonWriter);
        // 96, 19
        jamonWriter.write(" more regions in transition not shown)\n    ");
      }
      // 97, 11
      jamonWriter.write("\n    </section>\n");
    }
    // 99, 7
    jamonWriter.write("\n\n");
  }
  
  
}
