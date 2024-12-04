load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "adventofcode2024",
    testonly = True,
    srcs = glob(["*.kt"]),
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/com/knottysoftware/exercises/adventofcode/runner",
        "//src/main/kotlin/com/knottysoftware/exercises/adventofcode2024",
        "@maven//:junit_junit",
    ],
)
