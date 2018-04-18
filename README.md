
## Intellij Settings
 * Install Antlr4 plugin
 * Run "./sbt compile" or "sbt.bat - compile" or "compile" using sbt shell
 * Unmark source target/scala-2.12/src_managed/main/antlr4 in File - Project Structure - Modules if you want to build project with intellij
   * you should do this job every time you refresh sbt in Intellij

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
  *arith* -> **-** *arith* | *arith* **+** *arith* | *arith* **-** *arith* |  *arith* **\*** *arith* |  *arith* **/** *arith*  | integer | *lv* <br/>
  *cond* -> **!** *cond* | e **==** e | e **!=** e | *arith* **>** *arith* | *cond* **&&** *cond* | cond **||** cond | **true** | **false** | *lv* <br/>
