data class test(
    val contents: Contents?,
    val transportControls: TransportControls?
) {
    data class Contents(
        val singleColumnWatchNextResults: SingleColumnWatchNextResults?
    ) {
        data class SingleColumnWatchNextResults(
            val pivot: Pivot?,
            val results: Results?,
            val autoplay: Autoplay?
        ) {
            data class Pivot(
                val pivot: Pivot?
            ) {
                data class Pivot(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val shelfRenderer: ShelfRenderer?
                    ) {
                        data class ShelfRenderer(
                            val title: Title?,
                            val content: Content?
                        ) {
                            data class Title(
                                val runs: List<Run?>?,
                                val simpleText: String?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class Content(
                                val horizontalListRenderer: HorizontalListRenderer?
                            ) {
                                data class HorizontalListRenderer(
                                    val items: List<Item?>?
                                ) {
                                    data class Item(
                                        val tileRenderer: TileRenderer?,
                                        val gridVideoRenderer: GridVideoRenderer?,
                                        val pivotVideoRenderer: PivotVideoRenderer?,
                                        val tvMusicVideoRenderer: TvMusicVideoRenderer?
                                    ) {
                                        data class TileRenderer(
                                            val metadata: Metadata?
                                        ) {
                                            data class Metadata(
                                                val tileMetadataRenderer: TileMetadataRenderer?
                                            ) {
                                                data class TileMetadataRenderer(
                                                    val title: Title?
                                                ) {
                                                    data class Title(
                                                        val runs: List<Run?>?,
                                                        val simpleText: String?
                                                    ) {
                                                        data class Run(
                                                            val text: String?
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        data class GridVideoRenderer(
                                            val title: Title?,
                                            val shortBylineText: ShortBylineText?,
                                            val longBylineText: LongBylineText?,
                                            val thumbnail: Thumbnail?,
                                            val videoId: String?
                                        ) {
                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class ShortBylineText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class LongBylineText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class Thumbnail(
                                                val thumbnails: List<Thumbnail?>?
                                            ) {
                                                data class Thumbnail(
                                                    val url: String?,
                                                    val width: String?,
                                                    val height: String?
                                                )
                                            }
                                        }

                                        data class PivotVideoRenderer(
                                            val title: Title?,
                                            val thumbnail: Thumbnail?
                                        ) {
                                            data class Title(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class Thumbnail(
                                                val thumbnails: List<Thumbnail?>?
                                            ) {
                                                data class Thumbnail(
                                                    val url: String?,
                                                    val width: String?,
                                                    val height: String?
                                                )
                                            }
                                        }

                                        data class TvMusicVideoRenderer(
                                            val thumbnail: Thumbnail?,
                                            val primaryText: PrimaryText?,
                                            val secondaryText: SecondaryText?,
                                            val tertiaryText: TertiaryText?
                                        ) {
                                            data class Thumbnail(
                                                val thumbnails: List<Thumbnail?>?
                                            ) {
                                                data class Thumbnail(
                                                    val url: String?,
                                                    val width: String?,
                                                    val height: String?
                                                )
                                            }

                                            data class PrimaryText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class SecondaryText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class TertiaryText(
                                                val runs: List<Run?>?,
                                                val simpleText: String?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            data class Results(
                val results: Results?
            ) {
                data class Results(
                    val contents: List<Content?>?
                ) {
                    data class Content(
                        val itemSectionRenderer: ItemSectionRenderer?
                    ) {
                        data class ItemSectionRenderer(
                            val contents: List<Content?>?
                        ) {
                            data class Content(
                                val videoMetadataRenderer: VideoMetadataRenderer?,
                                val musicWatchMetadataRenderer: MusicWatchMetadataRenderer?
                            ) {
                                data class VideoMetadataRenderer(
                                    val owner: Owner?
                                ) {
                                    data class Owner(
                                        val videoOwnerRenderer: VideoOwnerRenderer?
                                    ) {
                                        class VideoOwnerRenderer
                                    }
                                }

                                class MusicWatchMetadataRenderer
                            }
                        }
                    }
                }
            }

            data class Autoplay(
                val autoplay: Autoplay?
            ) {
                data class Autoplay(
                    val sets: List<Set?>?,
                    val replayVideoRenderer: ReplayVideoRenderer?
                ) {
                    data class Set(
                        val nextVideoRenderer: NextVideoRenderer?
                    ) {
                        data class NextVideoRenderer(
                            val maybeHistoryEndpointRenderer: MaybeHistoryEndpointRenderer?,
                            val autoplayEndpointRenderer: AutoplayEndpointRenderer?
                        ) {
                            class MaybeHistoryEndpointRenderer

                            class AutoplayEndpointRenderer
                        }
                    }

                    class ReplayVideoRenderer
                }
            }
        }
    }

    data class TransportControls(
        val transportControlsRenderer: TransportControlsRenderer?
    ) {
        class TransportControlsRenderer
    }
}