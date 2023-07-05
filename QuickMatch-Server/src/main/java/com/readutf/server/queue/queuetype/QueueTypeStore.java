package com.readutf.server.queue.queuetype;

import com.readutf.quickmatch.shared.QueueType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueueTypeStore extends MongoRepository<QueueType, String> {


}
