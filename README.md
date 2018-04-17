
## Intellij Settings
 * Install Antlr4 plugin
 * Add target/scala-2.12/src_managed/main/antlr4 into File - settings - Build, Execution, Deployment - Compiler - Excludes
 * Run "./sbt compile" or "sbt.bat - compile" or "compile" using sbt shell

## Target Language
### Syntax
  *program* -> *function** main-*function*
  *function* -> **def** *name* *params* **:=** *block*
  *name* -> string
  *params* -> **(** variable* **)**
  *block* -> **{** *stmt** **}** | *stmt*
  *stmt* -> *lv*:=*e*
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *cond* **)** *block* **else** *block*
  &nbsp;&nbsp;&nbsp;&nbsp;| **while** **(** *cond* **)** *block*
  &nbsp;&nbsp;&nbsp;&nbsp;| **return** *e*
  *lv* -> variable
  *e* -> *arith* | *cond* | **(** e **)**
  *arith* -> *e* **+** *e* | *e* **-** *e* |  *e* **\*** *e* |  *e* **/** *e*  | integer
  *cond* -> e **==** e | e **!=** e | *arith* **>** *arith* | *cond* **&&** *cond* | cond **||** cond | **true** | **false**
