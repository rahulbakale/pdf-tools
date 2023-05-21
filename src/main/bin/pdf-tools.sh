script_dir_path="$(dirname "$(readlink -e "$0")")"

java -jar "${script_dir_path}/../libs/${project.artifactId}-${project.version}.jar" "$@"
