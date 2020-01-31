/*
   Copyright 2014-2020 Sam Gleske - https://github.com/samrocketman/jervis

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   */
/*
   Try reading a file from the current workspace, else fall back to attempting
   to read from a jervis submodule.
 */

tryReadFile = null
tryReadFile = { String file_path ->
    try {
        parent_job.readFileFromWorkspace(file_path).toString()
    }
    catch(Exception e) {
        parent_job.readFileFromWorkspace('jervis/' + file_path).toString()
    }
}
