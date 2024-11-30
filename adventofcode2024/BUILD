load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "app_lib",
    srcs = glob(["src/main/kotlin/com/knottysoftware/exercises/adventofcode2024/*.kt"]),
)

# There is a kt_jvm_binary rule, but it generates a bash script, which isn't so helpful on Windows.
java_binary(
    name = "app",
    main_class = "com.knottysoftware.exercises.adventofcode2024.AppKt",
    runtime_deps = [
        ":app_lib",
    ],
)