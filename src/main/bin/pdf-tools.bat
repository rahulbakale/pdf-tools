set "script_file_path=%0"
for %%a in ("%script_file_path%") do set "script_dir_path=%%~dpa"

java -jar %script_dir_path%\..\libs\${project.artifactId}-${project.version}.jar %*
