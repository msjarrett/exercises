load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")

kt_jvm_library(
    name = "adventofcode2024",
    srcs = glob(["*.kt"]),
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/kotlin/com/knottysoftware/exercises/adventofcode/common",
        "//src/main/kotlin/com/knottysoftware/exercises/adventofcode/runner",
        "@maven//:org_jetbrains_kotlinx_kotlinx_coroutines_core",
    ],
)
