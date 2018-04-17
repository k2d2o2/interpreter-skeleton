
## Intellij Settings
 * Install Antlr4 plugin
 * Add target/scala-2.12/src_managed/main/antlr4 into File - settings - Build, Execution, Deployment - Compiler - Excludes
 * Run "./sbt compile" or "sbt.bat - compile" or "compile" using sbt shell

## Target Language
### extension
* *.pa1
### Syntax
  *program* -> *function** main-*function*<br/>
  *function* -> **def** *name* *params* **:=** *block*<br/>
  *name* -> string<br/>
  *params* -> **(** variable* **)**<br/>
  *block* -> **{** *stmt** **}** | *stmt*<br/>
  *stmt* -> *lv*:=*e* **;**<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *cond* **)** *block* **else** *block*<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *cond* **)** *block* <br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **while** **(** *cond* **)** *block*<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **return** *e* **;**<br/>
  *lv* -> variable<br/>
  *e* -> *arith* | *cond* | **(** e **)** | *lv*<br/>
  *arith* -> **-** *arith* | *arith* **+** *arith* | *arith* **-** *arith* |  *arith* **\*** *arith* |  *arith* **/** *arith*  | integer <br/>
  *cond* -> **!** *cond* | e **==** e | e **!=** e | *arith* **>** *arith* | *cond* **&&** *cond* | cond **||** cond | **true** | **false** <br/>
