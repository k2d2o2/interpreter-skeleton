
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
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *e* **)** *block* **else** *block*<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **if** **(** *e* **)** *block* <br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **while** **(** *e* **)** *block*<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;| **return** *e* **;**<br/>
  *e* -> **(** e **)** | *arith* | **!** *e* | e **==** e | e **!=** e | *arith* **>** *arith* | *e* **&&** *e* | e **||** e | **true** | **false** <br/>
  *arith* -> **-** *arith* | *arith* **+** *arith* | *arith* **-** *arith* |  *arith* **\*** *arith* |  *arith* **/** *arith*  | integer | *lv* | f **(** *lv* **)** <br/>
  *lv* -> variable<br/>
