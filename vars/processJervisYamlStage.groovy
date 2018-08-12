/*
   Copyright 2014-2018 Sam Gleske - https://github.com/samrocketman/jervis

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
  This will process Jervis YAML in a Jenkins pipeline stage.
 */

import net.gleske.jervis.lang.lifecycleGenerator
import net.gleske.jervis.lang.pipelineGenerator

/**
  Returns a string which can be printed.  It is the decrypted properties from a
  .jervis.yml file.
 */
@NonCPS
String printDecryptedProperties(lifecycleGenerator generator, String credentials_id) {
    [
        "Attempting to decrypt jenkins.secrets using Jenkins Credentials ID ${credentials_id}.",
        'Decrypted the following properties (indented):',
        '    ' + generator.plainmap.keySet().join('\n    ')
    ].join('\n') as String
}

void call(lifecycleGenerator generator, pipelineGenerator pipeline_generator, List jervisEnvList, String script_header, String script_footer) {
    stage('Process Jervis YAML') {
        prepareJervisLifecycleGenerator(generator, 'github-token')
        pipeline_generator = new pipelineGenerator(generator)
        prepareJervisPipelineGenerator(pipeline_generator)
        //attempt to get the private key else return an empty string
        String credentials_id = generator.getObjectValue(generator.jervis_yaml, 'jenkins.secrets_id', '')
        if(credentials_id) {
            echo "DECRYPTED PROPERTIES\n${printDecryptedProperties(generator, credentials_id)}"
        }
        //end decrypting secrets

        script_header = loadCustomResource "header.sh"
        script_footer = loadCustomResource "footer.sh"
        jervisEnvList << "JERVIS_LANG=${generator.yaml_language}"
    }
}